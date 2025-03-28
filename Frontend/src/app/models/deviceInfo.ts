import {IpAddresses} from './ip-addresses';

export interface DeviceInfo {
  ipAddress: IpAddresses;
  mac: string;
  hostname: string;
  os: string;
  extraInfo: string;
}
