import { createSelector } from '@ngrx/store';
import {networkFeatureName} from './network.reducer';
import {NetworkState} from './network.state';

export const selectNetworkState = (state: any): NetworkState => state[networkFeatureName];

export const selectNetworks = createSelector(
  selectNetworkState,
  (state) => state.networkState
);
