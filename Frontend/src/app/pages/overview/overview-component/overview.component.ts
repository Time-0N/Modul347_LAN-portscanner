import {Component} from '@angular/core';
import {Store} from '@ngrx/store';
import {scanIp} from '../store/network-store/network.actions';
import {selectNetworks} from '../store/network-store/network.selectors';
import {FormsModule} from '@angular/forms';
import {NetworkListComponent} from '../network-list/network-list.component';
import {AsyncPipe, NgIf} from '@angular/common';
import {Network} from "../../../models/network";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

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
  ipToScan: string = '';
  searchTerm: string = '';

  networks$: Observable<Network[]>;
  filteredNetworks$: Observable<Network[]>;

  constructor(
    private readonly store: Store,
  ) {
    this.networks$ = this.store.select(selectNetworks);
    this.filteredNetworks$ = this.networks$.pipe(
        map(networks => this.filterNetworks(networks))
    )
  }

  private filterNetworks(networks: Network[]): Network[] {
    if (!this.searchTerm) return networks;
    const term = this.searchTerm.toLowerCase();
    return networks.filter(network =>
        network.name?.toLowerCase().includes(term)
    );
  }

  onSearchChange() {
    this.filteredNetworks$ = this.networks$.pipe(
        map(networks => this.filterNetworks(networks))
    )
  }

  onScanSubmit() {
    if (!this.ipToScan) return;
    this.store.dispatch(scanIp({ ip: this.ipToScan }));
  }
}
