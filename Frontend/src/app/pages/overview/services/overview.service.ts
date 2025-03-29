import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from 'rxjs';
import {Network} from '../../../models/network';

@Injectable({
  providedIn: 'root'
})
export class OverviewService {

  private api = "http://127.0.0.1:8080";

  constructor(private http: HttpClient) { }

  getNetworks(): Observable<Network[]> {
    return this.http.get<Network[]>(`${this.api}/api/getStoredNetworks`);
  }

  setNetworkName(network: Network, name: string): Observable<Network> {
    const headers = {
      'Content-Type': 'application/json'
    }
    return this.http.post<Network>(`${this.api}/api/${network.id}/name`, { name:name }, headers)
  }
}
