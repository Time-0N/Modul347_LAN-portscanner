import {Component} from '@angular/core';
import {AsyncPipe, JsonPipe} from '@angular/common';
import {Store} from '@ngrx/store';
import {selectNetworks} from '../store/network.selectors';
import {NetworkDatasource} from './network-datasource';


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
export class OverviewComponent {

  public readonly networksData$;

  constructor(
    private readonly store: Store
  ) {
    this.networksData$ = store.select(selectNetworks);
  }
}
