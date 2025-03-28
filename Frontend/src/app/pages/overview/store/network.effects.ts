import {inject, Injectable} from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import {of} from 'rxjs';
import {Network} from '../../../models/network';
import {NetworkScanService} from '../services/network-scan.service';
import {OverviewService} from '../services/overview.service';
import {networkResolverExecuted, loadNetworkSuccess, scanIp, scanIpSuccess, scanIpFailure} from './network.actions';
import {catchError, map, mergeMap} from 'rxjs/operators';

@Injectable()
export class NetworkEffects {

  actions$ = inject(Actions);
  overviewService = inject(OverviewService);
  networkScanService = inject(NetworkScanService);

  public readonly loadNetwork$ = createEffect(() =>
    this.actions$.pipe(
      ofType(networkResolverExecuted),
      mergeMap(() => {
        console.log("effect executed");
          return this.overviewService.getNetworks().pipe(
          map(networksArray => loadNetworkSuccess({networks: networksArray})),
        );
      })
    )
  );

  public readonly scanIp$ = createEffect(() =>
    this.actions$.pipe(
      ofType(scanIp),
      mergeMap(({ ip }) =>
        this.networkScanService.scanIp(ip).pipe(
          map((network: Network) => scanIpSuccess({ network })),
          catchError(error => of(scanIpFailure({ error })))
        )
      )
    )
  );
}
