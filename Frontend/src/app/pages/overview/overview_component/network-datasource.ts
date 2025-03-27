import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Network} from '../../../models/network';
import {OverviewService} from '../services/overview.service';

@Injectable()
export class NetworkDatasource {

  constructor(
    private readonly overviewService: OverviewService
  ) {

  }

  public loadData(): Observable<Network[]> {
    return this.overviewService.getNetworks()
  }

}
