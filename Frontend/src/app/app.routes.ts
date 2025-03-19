import {RouterModule, Routes} from '@angular/router';
import { OverviewComponent } from './components/overview/overview_component/overview.component';
import {NgModule} from '@angular/core';

export const routes: Routes = [
  { path: 'overview', component: OverviewComponent },
  { path: '', redirectTo: '/overview', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
