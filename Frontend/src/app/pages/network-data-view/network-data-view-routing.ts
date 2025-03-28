import {Routes} from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./network-overview/network-overview/network-overview-routing').then(mod => mod.routes)
  }
]
