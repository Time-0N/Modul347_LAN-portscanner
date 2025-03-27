import { ApplicationConfig, provideZoneChangeDetection, isDevMode } from '@angular/core';
import { provideRouter } from '@angular/router';
import {provideEffects} from '@ngrx/effects';
import {provideStore} from '@ngrx/store';

import { routes } from './app.routes';
import { provideStoreDevtools } from '@ngrx/store-devtools';
import {provideHttpClient} from '@angular/common/http';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideStore(),
    provideEffects([]),
    provideRouter(routes),
    provideHttpClient(),
    provideStoreDevtools({ maxAge: 25, logOnly: !isDevMode() })
  ]
};
