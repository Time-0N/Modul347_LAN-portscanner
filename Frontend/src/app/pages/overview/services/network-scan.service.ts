import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Network} from '../../../models/network';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class NetworkScanService {

  private api = environment.apiUrl;

  constructor(private http: HttpClient) { }

  scanIp(ip: string): Observable<Network> {
    return this.http.get<Network>(`${this.api}/scan-ips`, {
      params: { subnet: ip }
    });
  }
}
