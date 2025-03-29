import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Network} from '../../../models/network';

@Injectable({
  providedIn: 'root'
})
export class NetworkScanService {

  private api = "http://127.0.0.1:8080";

  constructor(private http: HttpClient) { }

  scanIp(ip: string): Observable<Network> {
    return this.http.get<Network>(`${this.api}/api/scan-ips`, {
      params: { subnet: ip }
    });
  }
}
