import {IpAddress} from './ipAddress';

export interface DeviceInfo {
  ipAddress: IpAddress;
  mac: String;
  hostname: String;
  os: String;
  extraInfo: String;
}
