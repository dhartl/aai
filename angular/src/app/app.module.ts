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
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NavbarComponent } from './navbar/navbar.component';
import { LayoutModule } from '@angular/cdk/layout';
import { MatToolbarModule, MatButtonModule, MatSidenavModule, MatIconModule, MatListModule } from '@angular/material';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
    LeafletModule.forRoot(),
    BrowserAnimationsModule,
    LayoutModule,
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    MatIconModule,
    MatListModule   
  ],
  declarations: [
    AppComponent,
    MapDoctorComponent,
    MapPharmacyComponent,
    NavbarComponent,
    
  ],
  providers:[
    	FacilitySearchService
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
