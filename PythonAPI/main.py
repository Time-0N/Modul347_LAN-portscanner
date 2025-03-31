from fastapi import FastAPI, Query
import socket
import subprocess
from fastapi import responses
import nmap
from scapy.all import ARP, Ether, srp
from concurrent.futures import ThreadPoolExecutor
from fastapi import FastAPI, HTTPException
import socket

app = FastAPI()

# ================== NETWORK-WIDE SCAN ================== #
@app.get("/scan")
def scan(subnet: str = Query("192.168.1.0/24", description="Subnet to scan (e.g., 192.168.1.0/24)")):
    """
    Scans the network for ALL possible devices using:
    - ARP (Scapy)
    - System ARP cache (`ip neigh show`)
    - Nmap Ping Scan (Backup)

    Returns: List of all detected devices with IPs, MAC addresses (if possible), and open ports.
    """
    devices = []

    # 🟢 1️⃣ ARP SCAN (Scapy) - Detects directly responding devices
    arp_request = ARP(pdst=subnet)
    broadcast = Ether(dst="ff:ff:ff:ff:ff:ff")
    arp_packet = broadcast / arp_request
    answered_list = srp(arp_packet, timeout=5, retry=3, verbose=False)[0]

    for sent, received in answered_list:
        devices.append({"ip": received.psrc, "mac": received.hwsrc, "source": "ARP"})

    # 🟢 2️⃣ SYSTEM ARP CACHE (`ip neigh show`) - Finds devices that were previously active
    result = subprocess.run(["ip", "neigh", "show"], capture_output=True, text=True)
    for line in result.stdout.split("\n"):
        parts = line.split()
        if len(parts) >= 5 and parts[2] == "lladdr":
            ip = parts[0]
            mac = parts[3]
            if ip not in [d["ip"] for d in devices]:  # Avoid duplicates
                devices.append({"ip": ip, "mac": mac, "source": "ARP Cache"})

    # 🟢 3️⃣ NMAP PING SCAN - Finds devices that ignore ARP (Windows, Firewalled Devices)
    scanner = nmap.PortScanner()
    scanner.scan(hosts=subnet, arguments="-sn")  # Ping scan (no port scanning)

    for ip in scanner.all_hosts():
        mac = scanner[ip]["addresses"].get("mac", "Unknown")
        if ip not in [d["ip"] for d in devices]:  # Avoid duplicates
            devices.append({"ip": ip, "mac": mac, "source": "Nmap Ping"})

    # 🟢 4️⃣ SCAN OPEN PORTS FOR EACH DEVICE (Multithreaded for speed)
    def scan_ports(ip):
        scanner = nmap.PortScanner()
        scanner.scan(ip, "1-1024", arguments="-T4")
        return [port for port in scanner[ip]["tcp"]] if ip in scanner.all_hosts() and "tcp" in scanner[ip] else []

    with ThreadPoolExecutor() as executor:
        future_ports = {executor.submit(scan_ports, dev["ip"]): dev for dev in devices}
        for future in future_ports:
            dev = future_ports[future]
            dev["open_ports"] = future.result()

    return {"devices": devices}


# ================== SINGLE DEVICE SCAN ================== #
@app.get("/device_scan")
def device_scan(
        ip: str = Query(..., description="IP address of the device to scan"),
        mode: str = Query("popular", description="Scan mode: popular, top1000, fulltcp, fulludp")
):
    """
    Scans a specified device (IP) for:
    - OS detection
    - Open ports based on scan mode
    - Banner grabbing for detected ports

    Scan modes:
    - `popular`  → Quick scan of the most common ports (Fastest)
    - `top1000`  → Nmap's Top 1000 ports (Balanced)
    - `fulltcp`  → Full TCP scan (0-65535) (Slow)
    - `fulludp`  → Full UDP scan (0-65535) (Slowest)

    Returns: OS info, open ports, and banners for the given IP.
    """

    # 🟢 1️⃣ OS Detection
    scanner = nmap.PortScanner()
    try:
        scanner.scan(ip, arguments="-O")  # OS Detection
        os_info = scanner[ip]["osmatch"][0]["name"] if "osmatch" in scanner[ip] else "Unknown"
    except:
        os_info = "Unknown"

    # 🟢 2️⃣ Select Scan Mode
    if mode == "popular":
        scan_args = "-p 21,22,23,25,53,80,110,135,139,143,443,445,3306,3389"
    elif mode == "top1000":
        scan_args = "--top-ports 1000"
    elif mode == "fulltcp":
        scan_args = "-p- -T4"
    elif mode == "fulludp":
        scan_args = "-p- -sU -T4"
    else:
        return {"error": "Invalid mode. Choose from: popular, top1000, fulltcp, fulludp."}

    # 🟢 3️⃣ Port Scanning
    scanner.scan(ip, arguments=scan_args)
    open_ports = [port for port in scanner[ip]["tcp"]] if ip in scanner.all_hosts() and "tcp" in scanner[ip] else []

    if mode == "fulludp":
        open_ports += [port for port in scanner[ip]["udp"]] if ip in scanner.all_hosts() and "udp" in scanner[ip] else []

    # 🟢 4️⃣ Banner Grabbing (Multi-threaded)
    def grab_banner(port):
        try:
            s = socket.socket()
            s.settimeout(1)
            s.connect((ip, int(port)))
            banner = s.recv(1024).decode().strip()
            s.close()
            return {"port": port, "service": banner}
        except:
            return {"port": port, "service": "Unknown"}

    banners = []
    with ThreadPoolExecutor() as executor:
        banners = list(executor.map(grab_banner, open_ports))

    return {
        "ip": ip,
        "os_info": os_info,
        "scan_mode": mode,
        "open_ports": open_ports,
        "banners": banners
    }
# ================== Healthcheck for DockerCompose ================== #
@app.get("/health")
async def health_check():
    """Ultra-simple health check that only verifies host network access"""
    try:
        # Test basic network connectivity by creating a raw socket
        with socket.socket(socket.AF_INET, socket.SOCK_RAW, socket.IPPROTO_ICMP) as s:
            s.settimeout(1)
            s.bind(('0.0.0.0', 0))  # Requires host network access
        return {"status": "healthy", "network_access": True}
    except PermissionError:
        # Raw sockets require privileges
        try:
            # Fallback to regular socket test
            with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
                s.settimeout(1)
                s.bind(('0.0.0.0', 0))
            return {"status": "healthy", "network_access": True}
        except Exception as e:
            raise HTTPException(status_code=503, detail=f"Network access failed: {str(e)}")
    except Exception as e:
        raise HTTPException(status_code=503, detail=f"Network access failed: {str(e)}")