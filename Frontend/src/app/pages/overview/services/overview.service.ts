import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from 'rxjs';
import {Network} from '../../../models/network';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OverviewService {

  private api = environment.apiUrl;

  constructor(
    private http: HttpClient,
  ) { }

  getNetworks(): Observable<Network[]> {
    return this.http.get<Network[]>(`${this.api}/getStoredNetworks`);
  }

  setNetworkName(network: Network, name: string): Observable<Network> {
    const url = `${this.api}/network/${network.id}/name`;
    return this.http.post<Network>(url, { name }, {
      headers: { 'Content-Type' : 'application/json' }
    });
  }
}
