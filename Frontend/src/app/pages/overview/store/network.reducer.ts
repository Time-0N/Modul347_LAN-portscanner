import { createReducer, on } from '@ngrx/store';
import { loadNetworkSuccess} from './network.actions';
import {NetworkState} from './network.state';

export const networkInitialState: NetworkState = {
  networkState: []
};

export const networkFeatureName = 'networkData';

export const networkReducer = createReducer(
  networkInitialState,

  on(loadNetworkSuccess, (state, action): NetworkState => {
    return {
      ...state,
      networkState: action.networks
    };
  })
);
