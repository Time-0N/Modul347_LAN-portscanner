import {IpAddress} from './ipAddress';

export interface Network {
  id: number;
  name: String;
  subnet: String;
  ipAddresses: IpAddress[];
}
