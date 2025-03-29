import {inject} from '@angular/core';
import {ResolveFn} from '@angular/router';
import {Store} from '@ngrx/store';
import {networkResolverExecuted} from '../store/network.actions';

export const resolveNetworkDataConfiguration: ResolveFn<void> = () => {
  const store = inject(Store);

  store.dispatch(networkResolverExecuted());

  return Promise.resolve();
}
