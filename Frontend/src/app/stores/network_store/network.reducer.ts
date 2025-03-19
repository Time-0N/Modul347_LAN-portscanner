import { createReducer, on } from '@ngrx/store';
import { loadNetwork, loadNetworkSuccess, loadNetworkFailure } from './network.actions';
import { Network } from '../../models/network';
import {state} from '@angular/animations';

export interface NetworkState {
  networks: Network[];
  loading: boolean;
  error: any;
}

export const initialState: NetworkState = {
  networks: [],
  loading: false,
  error: null
};

export const networkReducer = createReducer(
  initialState,
  on(loadNetwork, state => ({
    ...state,
    loading:true
  })),

  on(loadNetworkSuccess, (state, { networks }) => ({
    ...state,
    loading: false,
    networks
  })),

  on(loadNetworkFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  }))
);
