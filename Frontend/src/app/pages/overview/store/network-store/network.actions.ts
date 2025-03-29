import { createAction, props } from '@ngrx/store';
import { Network } from '../../../../models/network';

const actionName = '[network]';

export const networkResolverExecuted = createAction(`${actionName} resolver executed`);

export const loadNetworkSuccess =
  createAction(`${actionName} networkData loaded`, props< {
    networks: Network[]
  }>()
);

export const scanIp =
  createAction(`${actionName} scan IP`, props< {
    ip: string
  }>()
);

export const scanIpSuccess =
  createAction(`${actionName} scan IP success`, props< {
    network: Network
  }>()
);

export const scanIpFailure =
  createAction(`${actionName} scan IP failure`, props< {
    error: any
  }>()
);

export const updateNetwork = createAction(
  '[Network] Update Network',
  props<{ updatedNetwork: Network }>()
);
