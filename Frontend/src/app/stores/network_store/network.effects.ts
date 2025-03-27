import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { OverviewService } from '../../services/overview.service';
import { loadNetwork, loadNetworkSuccess, loadNetworkFailure } from './network.actions';
import { catchError, map, mergeMap } from 'rxjs/operators';
import { of } from 'rxjs';

@Injectable()
export class NetworkEffects {

  loadNetwork$ = createEffect(() =>
    this.actions$.pipe(
      ofType(loadNetwork),
      mergeMap(() => this.overviewService.getNetworks().pipe(
        map(networksArray => loadNetworkSuccess({ networks: networksArray })),
        catchError(error => of(loadNetworkFailure({ error })))
      ))
    )
  );

  constructor(
    private actions$: Actions,
    private overviewService: OverviewService
  ) {}
}
