import {inject, Injectable} from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import {groupBy, of, switchMap} from 'rxjs';
import {NetworkScanService} from '../../services/network-scan.service';
import {OverviewService} from '../../services/overview.service';
import {
  networkResolverExecuted,
  loadNetworkSuccess,
  scanIp,
  scanIpSuccess,
  scanIpFailure,
  updateNetworkName, updateNetworkNameSuccess, updateNetworkNameFailure
} from './network.actions';
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
      groupBy(action => action.ip),
      mergeMap(group$ =>
        group$.pipe(
          switchMap(({ ip }) =>
            this.networkScanService.scanIp(ip).pipe(
              map(network => scanIpSuccess({ network })),
              catchError(error => of(scanIpFailure({ error })))
            )
          )
        )
      )
    )
  );

  public readonly updateNetworkName$ = createEffect(() =>
    this.actions$.pipe(
      ofType(updateNetworkName),
      mergeMap(({ network, name}) =>
        this.overviewService.setNetworkName(network, name).pipe(
          map(updatedNetwork =>
            updateNetworkNameSuccess({ updatedNetwork })
          ),
          catchError(error =>
            of(updateNetworkNameFailure({ error }))
          )
        )
      )
    )
  )
}
