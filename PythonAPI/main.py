from fastapi import FastAPI
import socket
import nmap
from scapy.all import ARP, Ether, srp

app = FastAPI()

# Scan LAN for Devices (IP & MAC)
def scan_network():
    subnet = "192.168.1.0/24"  # Replace with your network subnet
    arp_request = ARP(pdst=subnet)
    broadcast = Ether(dst="ff:ff:ff:ff:ff:ff")
    arp_packet = broadcast / arp_request
    answered_list = srp(arp_packet, timeout=2, verbose=False)[0]

    devices = []
    for sent, received in answered_list:
        devices.append({"ip": received.psrc, "mac": received.hwsrc})
    return devices

# Function to Detect Device Type (Using Nmap OS Detection)
def get_device_type(ip):
    scanner = nmap.PortScanner()
    try:
        scanner.scan(ip, arguments="-O")
        if ip in scanner.all_hosts() and "osmatch" in scanner[ip]:
            device_type = scanner[ip]["osmatch"][0]["name"]
        else:
            device_type = "Unknown"
    except Exception as e:
        device_type = "Unknown"
    return device_type

# Port Scanning with Banner Grabbing
def scan_ports_and_grab_banner(ip):
    scanner = nmap.PortScanner()
    scanner.scan(ip, "1-1024")  # Scanning ports 1-1024

    open_ports_data = []

    if ip in scanner.all_hosts() and "tcp" in scanner[ip]:
        for port in scanner[ip]["tcp"]:
            banner = grab_banner(ip, port)  # Grab the banner right after finding the open port

            service = "Unknown"  # Default service
            if banner:
                service = banner.split("\n")[0]  # Use the first line of the banner for the service

            port_data = {
                "port": port,
                "service": service,
                "service_status": [
                    {
                        "service": service,
                        "running_data": banner if banner else "No banner found"
                    }
                ]
            }
            open_ports_data.append(port_data)

    return open_ports_data

# Banner Grabbing from Open Ports
def grab_banner(ip, port):
    try:
        s = socket.socket()
        s.settimeout(2)
        s.connect((ip, int(port)))
        banner = s.recv(1024).decode().strip()
        s.close()
        return banner
    except Exception as e:
        return None

# Full Scan Method: Running all tests (Port Scan, Banner Grabbing, Device Type Detection)
@app.get("/full-scan")
def full_scan():
    devices = scan_network()  # Find all devices in the network
    results = []

    for device in devices:
        device_name = get_device_type(device["ip"])  # Get device type using Nmap OS detection
        device_data = {
            "ip": device["ip"],
            "mac": device["mac"],
            "device_name": device_name,
            "open_ports": scan_ports_and_grab_banner(device["ip"])  # Now integrated
        }

        results.append(device_data)

    return {"devices": results}
