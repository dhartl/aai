import { NgModule }       from '@angular/core';
import { BrowserModule }  from '@angular/platform-browser';
import { FormsModule }    from '@angular/forms';
import { HttpClientModule }    from '@angular/common/http';
import { AppRoutingModule }     from './app-routing.module';
import {LeafletModule } from '@asymmetrik/ngx-leaflet';
import { AppComponent }         from './app.component';
import { MapDoctorComponent} from './maps/mapDoctor/mapDoctor.component';
import { MapPharmacyComponent } from './maps/mapPharmacy/mapPharmacy.component';
import { FacilitySearchService } from './services/facilitysearch.service';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
    LeafletModule.forRoot()   
  ],
  declarations: [
    AppComponent,
    MapDoctorComponent,
    MapPharmacyComponent,
    
  ],
  providers:[
    	FacilitySearchService
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
