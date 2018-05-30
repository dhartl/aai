import { NgModule }       from '@angular/core';
import { BrowserModule }  from '@angular/platform-browser';
import { FormsModule }    from '@angular/forms';
import { HttpClientModule }    from '@angular/common/http';
import { AppRoutingModule }     from './app-routing.module';
import {LeafletModule } from '@asymmetrik/ngx-leaflet';
import { AppComponent }         from './app.component';
import { MapDoctorComponent} from './maps/mapDoctor/mapDoctor.component';
import { FacilitySearchService } from './services/facilitysearch.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LayoutModule } from '@angular/cdk/layout';
import {MatDialogModule, MatSliderModule, MatCheckboxModule} from '@angular/material';
import { MatToolbarModule, MatButtonModule, MatSidenavModule, MatIconModule, MatListModule } from '@angular/material';
import { DialogPharmacyComponent } from './dialog-pharmacy/dialog-pharmacy.component';
import {MatCardModule} from '@angular/material';
import { PhDialogComponent } from './ph-dialog/ph-dialog.component';
import { LeafletMarkerClusterModule } from '@asymmetrik/ngx-leaflet-markercluster';



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
    MatSliderModule,
    MatCheckboxModule,
    LeafletMarkerClusterModule
  ],
  entryComponents: [PhDialogComponent],
  declarations: [
    AppComponent,
    MapDoctorComponent,
    DialogPharmacyComponent,
    PhDialogComponent,    
  ],
  providers:[
    	FacilitySearchService
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
