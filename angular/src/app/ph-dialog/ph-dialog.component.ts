import { Component, OnInit, Inject  } from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material';
import {MatDialogRef} from '@angular/material';
import { FacilitySearchService } from '../services/facilitysearch.service';
import { DoctorRequest } from '../entities/doctorRequest';


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
    this.thisDialogRef.close(this.doctorRequest);
    
  }

  onCloseCancel() {
    this.thisDialogRef.close(this.doctorRequest);
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
