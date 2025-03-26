import { ApplicationConfig, provideZoneChangeDetection, isDevMode } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideStore } from '@ngrx/store';
import { provideEffects } from '@ngrx/effects';
import { provideStoreDevtools } from '@ngrx/store-devtools';
import {networkReducer} from './stores/network_store/network.reducer';
import {NetworkEffects} from './stores/network_store/network.effects';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),

    provideStore({ networkState: networkReducer }),
    provideEffects(NetworkEffects),

    provideStoreDevtools({ maxAge: 25, logOnly: !isDevMode() })]
};
