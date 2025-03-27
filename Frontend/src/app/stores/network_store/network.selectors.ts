import { createFeatureSelector, createSelector } from '@ngrx/store';
import { NetworkState } from './network.reducer';

export const selectNetworkState = createFeatureSelector<NetworkState>('network');

export const selectNetworks = createSelector(
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
