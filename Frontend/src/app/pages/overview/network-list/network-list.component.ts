import {CommonModule, NgIf} from '@angular/common';
import {Component, Input} from '@angular/core';
import {Network} from '../../../models/network';
import {FormsModule} from '@angular/forms';
import {OverviewService} from '../services/overview.service';
import {Store} from "@ngrx/store";
import {updateNetworkName} from "../store/network-store/network.actions";

@Component({
  selector: 'app-network-list',
  standalone: true,
  imports: [
    NgIf,
    CommonModule,
    FormsModule
  ],
  templateUrl: './network-list.component.html',
  styleUrl: './network-list.component.css'
})
export class NetworkListComponent {
  @Input() networks: Network[] = [];
  @Input() updateNetworkName: (network: Network, name: string) => void = () => {};

  expandedNetworkId: number | null = null;
  expandedIp: string | null = null;

  constructor(private readonly store: Store,) {
  }

  toggleNetwork(id: number) {
    this.expandedNetworkId = this.expandedNetworkId === id ? null : id;
    this.expandedIp = null;
  }

  toggleIp(ip: string) {
    this.expandedIp = this.expandedIp === ip ? null : ip;
  }

  setOrUpdateNetworkName(network: Network, newName: string) {
    if (newName !== network.name) {
      this.store.dispatch(updateNetworkName({
        network,
        name: newName
      }))
    }
  }
}
