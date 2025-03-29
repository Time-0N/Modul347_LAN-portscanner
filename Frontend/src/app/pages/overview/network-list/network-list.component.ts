import {CommonModule, NgIf} from '@angular/common';
import {Component, Input} from '@angular/core';
import {Network} from '../../../models/network';

@Component({
  selector: 'app-network-list',
  standalone: true,
  imports: [
    NgIf,
    CommonModule
  ],
  templateUrl: './network-list.component.html',
  styleUrl: './network-list.component.css'
})
export class NetworkListComponent {
  @Input() networks: Network[] = [];

  expandedNetworkId: number | null = null;
  expandedIp: string | null = null;

  toggleNetwork(id: number) {
    this.expandedNetworkId = this.expandedNetworkId === id ? null : id;
    this.expandedIp = null;
  }

  toggleIp(ip: string) {
    this.expandedIp = this.expandedIp === ip ? null : ip;
  }
}
