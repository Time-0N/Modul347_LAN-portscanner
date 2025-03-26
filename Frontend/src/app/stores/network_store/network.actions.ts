import { createAction, props } from '@ngrx/store';
import { Network } from '../../models/network';

export const loadNetwork = createAction('[Network] Load Network');

export const loadNetworkSuccess = createAction(
  '[Network] Load Network Success',
  props<{ networks: Network[] }>()
);

export const loadNetworkFailure = createAction(
  '[Network] Load Network Failure',
  props<{ error: any }>()
);
