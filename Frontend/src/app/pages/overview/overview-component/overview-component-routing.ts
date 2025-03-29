import {Routes} from '@angular/router';
import {provideEffects} from '@ngrx/effects';
import {provideState} from '@ngrx/store';
import {NetworkEffects} from '../store/network-store/network.effects';
import {OverviewComponent} from './overview.component';
import {resolveNetworkDataConfiguration} from '../services/network-data-resolve.service';
import {networkFeatureName, networkReducer} from '../store/network-store/network.reducer';

export const routes: Routes = [
  {
    path: '',
    component: OverviewComponent,
    providers: [
      provideState(networkFeatureName, networkReducer),
      provideEffects([NetworkEffects])
    ],
    resolve: {
      networkDataResolver: resolveNetworkDataConfiguration
    }
  }
];
