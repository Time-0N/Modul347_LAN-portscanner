import {Component} from '@angular/core';
import {AsyncPipe, JsonPipe} from '@angular/common';
import {Store} from '@ngrx/store';
import {scanIp} from '../store/network.actions';
import {selectNetworks} from '../store/network.selectors';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-overview',
  standalone: true,
  imports: [
    AsyncPipe,
    JsonPipe,
    FormsModule
  ],
  templateUrl: './overview.component.html',
  styleUrl: './overview.component.css'
})
export class OverviewComponent {

  public readonly networksData$;
  ipToScan: string = '';

  constructor(
    private readonly store: Store,
  ) {
    this.networksData$ = store.select(selectNetworks);
  }

  onScanSubmit() {
    if (!this.ipToScan) return;
    this.store.dispatch(scanIp({ ip: this.ipToScan }));
  }
}
