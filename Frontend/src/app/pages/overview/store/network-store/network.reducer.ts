import { createReducer, on } from '@ngrx/store';
import {loadNetworkSuccess, scanIpSuccess, updateNetworkNameSuccess} from './network.actions';
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
  }),

  on(scanIpSuccess, (state, { network }) => {
    const exists = state.networkState.some(n => n.id === network.id);

    const updateNetworkState = exists
      ? state.networkState.map(n => n.id === network.id ? network : n)
      : [...state.networkState, network];

    return {
      ...state,
      networkState: updateNetworkState
    };
  }),

  on(updateNetworkNameSuccess, (state, { updatedNetwork }) => ({
    ...state,
    networkState: state.networkState.map(network =>
      network.id === updatedNetwork.id ? updatedNetwork : network
    )
  })),
);
