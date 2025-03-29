import { Routes } from '@angular/router';
import {DummyComponent} from './pages/dummy/dummy_component/dummy/dummy.component';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    component: DummyComponent
  },
  {
    path: 'overview',
    loadChildren: () => import('./pages/overview/overview-routing').then(mod => mod.routes)
  }
];
