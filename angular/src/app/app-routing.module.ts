import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MapDoctorComponent} from './maps/mapDoctor/mapDoctor.component';
const routes: Routes = [
  { path: '', redirectTo: '/mapDoctor', pathMatch: 'full' },
  { path: 'mapDoctor', component: MapDoctorComponent  },
  
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
