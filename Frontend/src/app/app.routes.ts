import { Routes } from '@angular/router';
import { OverviewComponent } from './components/overview/overview_component/overview.component';

export const routes: Routes = [
  {
    path: '',
    component: OverviewComponent
  },
  {
    path: 'overview',
    component: OverviewComponent
  },
  {
    path: '**',
    redirectTo: ''
  }
];
