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

export const updateNetworkName =
    createAction(`${actionName} Update Network Name`, props<{
        network: Network, name: string
    }>()
);

export const updateNetworkNameSuccess = createAction(
    `${actionName} Update Network Name Success`,
    props<{ updatedNetwork: Network }>()
);

export const updateNetworkNameFailure = createAction(
    `${actionName} Update Network Name Failure`,
    props<{ error: any }>()
);

export const rescanNetwork =
  createAction(`${actionName} rescan Network`, props<{
    networkId: number
  }>()
);

export const rescanNetworkSuccess =
  createAction(`${actionName} rescan Network Success`, props<{
    updatedNetwork: Network
  }>()
);
