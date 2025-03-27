import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {Network} from '../../../models/network';
import {loadNetwork} from '../../../stores/network_store/network.actions';
import {Store} from '@ngrx/store';
import { selectNetworks } from '../../../stores/network_store/network.selectors';
import {AsyncPipe, JsonPipe} from '@angular/common';


@Component({
  selector: 'app-overview',
  standalone: true,
  imports: [
    AsyncPipe,
    JsonPipe
  ],
  templateUrl: './overview.component.html',
  styleUrl: './overview.component.css'
})
export class OverviewComponent implements OnInit{

  networks$: Observable<Network[]>;

  constructor(private store: Store) {
    console.log('OverviewComponent LOADED');

    this.networks$ = this.store.select(selectNetworks);
  }

  ngOnInit(): void {
    console.log('OverviewComponent instantiated')

    this.store.dispatch(loadNetwork());
  }
}
