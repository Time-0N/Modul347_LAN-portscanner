import {Component} from '@angular/core';
import {Store} from '@ngrx/store';
import {scanIp, updateNetwork} from '../store/network-store/network.actions';
import {selectNetworks} from '../store/network-store/network.selectors';
import {FormsModule} from '@angular/forms';
import {NetworkListComponent} from '../network-list/network-list.component';
import {AsyncPipe, NgIf} from '@angular/common';
import {Network} from '../../../models/network';

@Component({
  selector: 'app-overview',
  standalone: true,
  imports: [
    FormsModule,
    NetworkListComponent,
    AsyncPipe,
    NgIf
  ],
  templateUrl: './overview.component.html',
  styleUrl: './overview.component.css'
})
export class OverviewComponent {

  public readonly networks$;
  ipToScan: string = '';

  constructor(
    private readonly store: Store,
  ) {
    this.networks$ = store.select(selectNetworks);
  }

  onScanSubmit() {
    if (!this.ipToScan) return;
    this.store.dispatch(scanIp({ ip: this.ipToScan }));
  }

  updateNetworkName(network: Network, name: string) {
    if (network.name !== name) {
      this.store.dispatch(updateNetwork({ updatedNetwork: { ...network, name } }))
    }
  }
}
