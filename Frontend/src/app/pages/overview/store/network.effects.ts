import {inject, Injectable} from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import {OverviewService} from '../services/overview.service';
import { networkResolverExecuted, loadNetworkSuccess } from './network.actions';
import { map, mergeMap } from 'rxjs/operators';

@Injectable()
export class NetworkEffects {

  actions$ = inject(Actions);
  overviewService = inject(OverviewService);

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
}
