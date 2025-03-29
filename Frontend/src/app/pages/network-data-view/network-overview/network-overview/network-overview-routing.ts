import {Routes} from '@angular/router';
import {provideEffects} from '@ngrx/effects';
import {provideState} from '@ngrx/store';
import {resolveNetworkDataConfiguration} from '../../../overview/services/network-data-resolve.service';
import {NetworkEffects} from '../../../overview/store/network.effects';
import {networkFeatureName, networkReducer} from '../../../overview/store/network.reducer';
import {NetworkOverviewComponent} from './network-overview.component';

export const routes: Routes = [
  {
    path: '',
    component: NetworkOverviewComponent,
    providers: [
      provideState(networkFeatureName, networkReducer),
      provideEffects([NetworkEffects])
    ],
    resolve: {
      networkDataResolver: resolveNetworkDataConfiguration
    }
  }
];
