import { createFeatureSelector, createSelector } from '@ngrx/store';
import { NetworkState } from './network.reducer';
import {state} from '@angular/animations';

export const selectNetworkState = createFeatureSelector<NetworkState>('network');

export const selectNetwork = createSelector(
  selectNetworkState,
  (state) => state.networks
);

export const selectLoading = createSelector(
  selectNetworkState,
  (state) => state.loading
);

export const selectError = createSelector(
  selectNetworkState,
  (state) => state.error
);
