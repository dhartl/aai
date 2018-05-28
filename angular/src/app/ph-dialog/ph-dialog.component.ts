import { Component, OnInit, Inject  } from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material';
import {MatDialogRef, MatSliderModule} from '@angular/material';
import { FacilitySearchService } from '../services/facilitysearch.service';
import { DoctorRequest } from '../entities/doctorRequest';
import { HeatmapRequest } from '../entities/heatmapRequest';


@Component({
  selector: 'app-ph-dialog',
  templateUrl: './ph-dialog.component.html',
  styleUrls: ['./ph-dialog.component.css']
})
export class PhDialogComponent implements OnInit {

  constructor(public thisDialogRef: MatDialogRef<PhDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: string, private _getSettingData:FacilitySearchService) { }

  doctorRequest : DoctorRequest={
   insuranceIds: [],
   specialityIds: []
  }
  heatmapRequest : HeatmapRequest={
    doctorRequest : this.doctorRequest,
    maxDistanceInMeter: 2000
  }

  formatLabel(value: number | null) {
    if (!value) {
      return 0;
    }

    if (value >= 1000) {
      return Math.round(value / 1000) + 'k';
    }

    return value;
  }

  writeData(){
    this.doctorRequest.insuranceIds = [];
    this.doctorRequest.specialityIds = [];
    for(var ins of this.insurances)
    {
      if(ins.isChecked)
      {
        this.doctorRequest.insuranceIds.push(ins.id);
      }
    }
    for(var spec of this.specialities)
    {
      if(spec.isChecked)
      {
        this.doctorRequest.specialityIds.push(spec.id);
      }
    }

    this.heatmapRequest.doctorRequest = this.doctorRequest;
  }

  deselectionInsurances:boolean = true;

  insurances = [{id:1, name:"halllo" ,isChecked: false},
                {id:2, name:"hallo2" ,isChecked: true}]

  specialities = [{id:1, name:"halllo" ,isChecked: false},
  {id:2, name:"hallo2" ,isChecked: true}]

  
  ngOnInit() {
    this._getSettingData.getInsurances().subscribe((data) =>{
    this.insurances = data;
    this.insurances.forEach(x=> x.isChecked = true);
      } );
      this._getSettingData.getSpecialities().subscribe((data) =>{
        this.specialities = data;
        this.specialities.forEach(x=> x.isChecked = true);
          } );
  }

  onCloseConfirm() {
    this.writeData();
    this.thisDialogRef.close(this.heatmapRequest);
    
  }

  onCloseCancel() {
    this.thisDialogRef.close(this.heatmapRequest);
  }

  onDeSelectAllInsurances()
  { 
    for(var ins of this.insurances)
    {
      if(this.deselectionInsurances)
    {
      ins.isChecked = false;
     
    }
    else{
      ins.isChecked = true;
    }
  }
  if(this.deselectionInsurances)
  {
    this.deselectionInsurances = false;
  }

    else{this.deselectionInsurances = true;}
  }

  deselectionSpecialites : boolean = true;

  onDeSelectAllSpecialities()
  { 
    for(var specs of this.specialities)
    {
      if(this.deselectionSpecialites)
    {
      specs.isChecked = false;
      
    }
    else{
      specs.isChecked = true;
    }
  }
  if (this.deselectionSpecialites)
  {
    this.deselectionSpecialites = false;
  }
  else{
    this.deselectionSpecialites = true;
  }}}
