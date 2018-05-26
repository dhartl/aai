import { Component, ViewChild, AfterViewInit } from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import { NgMaterialModule } from './ngmaterial.module';
import {MatDialog} from '@angular/material';
import { PhDialogComponent } from './ph-dialog/ph-dialog.component';
import { FacilitySearchService } from './services/facilitysearch.service';
import { DoctorRequest } from './entities/doctorRequest';
import { HeatmapRequest } from './entities/heatmapRequest';
import { DialogPharmacyComponent } from './dialog-pharmacy/dialog-pharmacy.component';
import { MapDoctorComponent } from './maps/mapDoctor/mapDoctor.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent  {
  
  @ViewChild(MapDoctorComponent) child;

  

 
  constructor(public dialog: MatDialog, private _DoctorSearch: FacilitySearchService ) {
    _DoctorSearch.getInsurances();
    _DoctorSearch.tryAPI();
  }
  // ngAfterViewInit(): void {
  //   this.doctorRequest = this.child.dialogRequest as DoctorRequest;
  // }
  
  doctorRequest : DoctorRequest = {
    insuranceIds: [9],
    specialityIds: []

  };

  changeMapSettings(dr: DoctorRequest)
  {
    this.doctorRequest = dr;
    this.child.doctorRequest = dr;
    this.child.changeData();
  }
  
  title = 'Pharmacy Location Analyzer - BPJ Campus 02 ';

  


}
