from fastapi import FastAPI, Query, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
import socket
import subprocess
import nmap
from scapy.all import ARP, Ether, srp
from concurrent.futures import ThreadPoolExecutor
import logging
from typing import List, Dict, Optional
import os

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(name)s - %(levelname)s - %(message)s"
)
logger = logging.getLogger(__name__)

app = FastAPI(
    title="Network Scanner API",
    description="Integrated with Spring Boot Backend",
    version="1.0.0"
)

# CORS Configuration
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # In production, specify your Angular/Spring Boot URLs
    allow_credentials=True,
    allow_methods=["GET"],
    allow_headers=["*"],
)

def is_valid_ip(ip: str) -> bool:
    """Validate IP address format"""
    try:
        socket.inet_aton(ip)
        return True
    except socket.error:
        return False

def is_valid_subnet(subnet: str) -> bool:
    """Basic subnet validation"""
    try:
        if '/' not in subnet:
            return False
        ip, mask = subnet.split('/')
        return is_valid_ip(ip) and 0 <= int(mask) <= 32
    except ValueError:
        return False

@app.get("/scan", response_model=Dict[str, List[Dict[str, str]]])
async def network_scan(
        subnet: str = Query(
            "192.168.1.0/24",
            description="Subnet to scan in CIDR notation",
            regex=r"^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}/\d{1,2}$"
        )
):
    """
    Comprehensive network scan combining:
    - ARP scanning (Scapy)
    - System ARP cache
    - Nmap ping sweep
    """
    if not is_valid_subnet(subnet):
        raise HTTPException(status_code=400, detail="Invalid subnet format")

    devices = []
    logger.info(f"Starting network scan for subnet: {subnet}")

    try:
        # 1. ARP Scan (Scapy)
        arp_request = ARP(pdst=subnet)
        broadcast = Ether(dst="ff:ff:ff:ff:ff:ff")
        arp_packet = broadcast / arp_request
        answered = srp(arp_packet, timeout=2, verbose=0)[0]

        for _, received in answered:
            devices.append({
                "ip": received.psrc,
                "mac": received.hwsrc.upper(),
                "source": "ARP"
            })

        # 2. System ARP Cache
        try:
            result = subprocess.run(["ip", "neigh", "show"], capture_output=True, text=True)
            for line in result.stdout.split('\n'):
                parts = line.split()
                if len(parts) >= 5 and parts[2] == "lladdr":
                    ip = parts[0]
                    mac = parts[3].upper()
                    if ip not in [d["ip"] for d in devices]:
                        devices.append({
                            "ip": ip,
                            "mac": mac,
                            "source": "ARP Cache"
                        })
        except Exception as e:
            logger.warning(f"Failed to read ARP cache: {str(e)}")

        # 3. Nmap Ping Scan
        try:
            scanner = nmap.PortScanner()
            scanner.scan(hosts=subnet, arguments="-sn")

            for ip in scanner.all_hosts():
                if ip not in [d["ip"] for d in devices]:
                    mac = scanner[ip]["addresses"].get("mac", "unknown").upper()
                    devices.append({
                        "ip": ip,
                        "mac": mac,
                        "source": "Nmap Ping"
                    })
        except Exception as e:
            logger.error(f"Nmap ping scan failed: {str(e)}")
            raise HTTPException(status_code=500, detail="Nmap scan failed")

        # 4. Port Scanning (Basic)
        def scan_ports(ip: str) -> List[int]:
            try:
                scanner = nmap.PortScanner()
                scanner.scan(ip, "21-23,80,443,3389", arguments="-T4")
                return list(scanner[ip]["tcp"].keys()) if "tcp" in scanner[ip] else []
            except:
                return []

        with ThreadPoolExecutor(max_workers=5) as executor:
            port_results = list(executor.map(scan_ports, [d["ip"] for d in devices]))
            for device, ports in zip(devices, port_results):
                device["ports"] = ports

        return {"devices": devices}

    except Exception as e:
        logger.error(f"Network scan failed: {str(e)}")
        raise HTTPException(status_code=500, detail="Scan failed")

@app.get("/device_scan", response_model=Dict[str, str])
async def device_scan(
        ip: str = Query(..., description="Target IP address"),
        mode: str = Query("popular", description="Scan mode: popular, top1000")
):
    """Deep scan of individual device"""
    if not is_valid_ip(ip):
        raise HTTPException(status_code=400, detail="Invalid IP address")

    valid_modes = ["popular", "top1000"]
    if mode not in valid_modes:
        raise HTTPException(
            status_code=400,
            detail=f"Invalid mode. Use: {', '.join(valid_modes)}"
        )

    logger.info(f"Starting {mode} scan for IP: {ip}")

    try:
        scanner = nmap.PortScanner()

        # OS Detection
        scanner.scan(ip, arguments="-O")
        os_info = scanner[ip]["osmatch"][0]["name"] if "osmatch" in scanner[ip] else "unknown"

        # Port Scanning
        scan_args = {
            "popular": "-p 21-23,80,443,3389",
            "top1000": "--top-ports 1000"
        }[mode]

        scanner.scan(ip, arguments=scan_args)
        open_ports = list(scanner[ip]["tcp"].keys()) if "tcp" in scanner[ip] else []

        # Banner Grabbing
        banners = []
        for port in open_ports:
            try:
                with socket.socket() as s:
                    s.settimeout(1)
                    s.connect((ip, port))
                    banner = s.recv(1024).decode().strip()
                    banners.append({
                        "port": port,
                        "banner": banner if banner else "No banner"
                    })
            except:
                banners.append({"port": port, "banner": "Unavailable"})

        return {
            "ip": ip,
            "os": os_info,
            "open_ports": open_ports,
            "banners": banners,
            "scan_mode": mode
        }

    except Exception as e:
        logger.error(f"Device scan failed: {str(e)}")
        raise HTTPException(status_code=500, detail="Device scan failed")

@app.get("/health")
async def health_check():
    """Endpoint for Docker health checks"""
    return {"status": "healthy", "service": "network-scanner"}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(
        app,
        host="0.0.0.0",
        port=int(os.getenv("PORT", 8081)),
        log_level="info",
        timeout_keep_alive=30
    )