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
import {MatDialogModule} from '@angular/material';
import { MatToolbarModule, MatButtonModule, MatSidenavModule, MatIconModule, MatListModule } from '@angular/material';
import { DialogPharmacyComponent } from './dialog-pharmacy/dialog-pharmacy.component';
import {MatCardModule} from '@angular/material';
import { PhDialogComponent } from './ph-dialog/ph-dialog.component';

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
    LeafletModule.forRoot(),
    LayoutModule,
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    MatIconModule,
    MatListModule ,
    MatCardModule,
    MatDialogModule,
  ],
  entryComponents: [PhDialogComponent],
  declarations: [
    AppComponent,
    MapDoctorComponent,
    MapPharmacyComponent,
    NavbarComponent,
    DialogPharmacyComponent,
    PhDialogComponent,    
  ],
  providers:[
    	FacilitySearchService
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
