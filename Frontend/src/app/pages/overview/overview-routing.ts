import {Routes} from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./overview-component/overview-component-routing').then(mod => mod.routes)
  }
]
