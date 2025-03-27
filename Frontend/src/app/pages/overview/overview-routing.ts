import {Routes} from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./overview_component/overview-component-routings').then(mod => mod.routes)
  }
]
