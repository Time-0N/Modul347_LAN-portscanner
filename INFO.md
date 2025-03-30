Modul347_LAN-portscanner
Beschreibung
Das Modul347_LAN-portscanner Projekt ist eine applikation, mit der man Netzwerke scannen, Endpunkte suchen und Geräteinformationen anzeigen kann. Über eine einfache Weboberfläche lassen sich diese Funktionen nutzen.

API Endpunkte
GET /api/scan-ips?subnet={subnet}: Scannt das angegebene Subnetz nach IP-Adressen.
GET /api/full-scan/{ipId}: Führt einen vollständigen Scan für eine bestimmte IP-Adresse durch.
GET /api/list-ips: Listet alle gescannten IP-Adressen.
GET /api/device-info/{ipId}: Zeigt Informationen zu einem Gerät anhand der IP-Adresse.

Docker Compose
Um das Projekt zu starten, benutze einfach:

docker-compose -f docker-compose-build.yml up --build

Dies startet Backend, Frontend und die Datenbank.

