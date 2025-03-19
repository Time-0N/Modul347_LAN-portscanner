import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {Network} from '../models/network';

@Injectable({
  providedIn: 'root'
})
export class OverviewService {

  private api = "http://127.1.0.0";

  constructor(private http: HttpClient) { }

  getNetworks(): Observable<Network[]> {
    return this.http.get<Network[]>(this.api);
  }
}
