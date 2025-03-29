import {CommonModule} from '@angular/common';
import { Component } from '@angular/core';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs';
import {Network} from '../../../../models/network';
import {selectNetworks} from '../../../overview/store/network.selectors';
import {NetworkListComponent} from '../../network-list/network-list.component';

@Component({
  selector: 'app-network-overview',
  imports: [
    CommonModule,
    NetworkListComponent
  ],
  templateUrl: './network-overview.component.html',
  styleUrl: './network-overview.component.css'
})
export class NetworkOverviewComponent {
  networks$: Observable<Network[]>;

  constructor(
    private store: Store
  ) {
    this.networks$ = this.store.select(selectNetworks)
  }
}
