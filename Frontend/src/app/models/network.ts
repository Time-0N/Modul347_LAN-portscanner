import {IpAddresses} from './ip-addresses';

export interface Network {
  id: number;
  name: string;
  subnet: string;
  ipAddresses: IpAddresses[];
}
