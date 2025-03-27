import { createAction, props } from '@ngrx/store';
import { Network } from '../../../models/network';

const actionName = '[network]';

export const networkResolverExecuted = createAction(`${actionName} resolver executed`);

export const loadNetworkSuccess =
  createAction(`${actionName} networkData loaded`, props< {
    networks: Network[]
  }>()
);
