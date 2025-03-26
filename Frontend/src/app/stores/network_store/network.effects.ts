import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { OverviewService } from '../../services/overview.service';
import { loadNetwork, loadNetworkSuccess, loadNetworkFailure } from './network.actions';
import { catchError, map, mergeMap } from 'rxjs';
import { of } from 'rxjs';

@Injectable()
export class NetworkEffects {

  loadNetwork$ = createEffect(() =>
    this.actions$.pipe(
      ofType(loadNetwork),
      mergeMap(() => this.overviewServie.getNetworks().pipe(
        map(networksArray => loadNetworkSuccess({ networks: networksArray })),
        catchError(error => of(loadNetworkFailure({ error })))
      ))
    )
  );

  constructor(
    private actions$: Actions,
    private overviewServie: OverviewService
  ) {}
}
