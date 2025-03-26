import { Component } from '@angular/core';
import {Observable} from 'rxjs';
import {Network} from '../../../models/network';
import {loadNetwork} from '../../../stores/network_store/network.actions';
import {Store} from '@ngrx/store';
import { selectNetwork } from '../../../stores/network_store/network.selectors';
import {AsyncPipe, JsonPipe} from '@angular/common';


@Component({
  selector: 'app-overview',
  imports: [
    AsyncPipe,
    JsonPipe
  ],
  templateUrl: './overview.component.html',
  styleUrl: './overview.component.css'
})
export class OverviewComponent {

  networks$: Observable<Network[]>;

  constructor(private store: Store) {
    this.networks$ = this.store.select(selectNetwork);
  }

  ngOnInit(): void {
    this.store.dispatch(loadNetwork());
  }
}
