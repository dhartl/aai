import { Component, OnInit , Input, EventEmitter, Output} from '@angular/core';
import { MatDialog } from '@angular/material';
import { PhDialogComponent } from '../ph-dialog/ph-dialog.component';
import { DoctorRequest } from '../entities/doctorRequest';
import { HeatmapRequest } from '../entities/heatmapRequest';

@Component({
  selector: 'app-dialog-pharmacy',
  templateUrl: './dialog-pharmacy.component.html',
  styleUrls: ['./dialog-pharmacy.component.css']
})
export class DialogPharmacyComponent implements OnInit {

  @Input()
  dialogResult: HeatmapRequest = {
    doctorRequest: {
    insuranceIds: [],
    specialityIds: []
    },
    maxDistanceInMeter: 2000,
    }

  @Output() changed = new EventEmitter<HeatmapRequest>();
  

  constructor(public dialog: MatDialog) {
    // this.dialogResult.insuranceIds = [];
    // this.dialogResult.insuranceIds = [];
  }

  
  ngOnInit() {
  }
  openDialog() {
    let dialogRef = this.dialog.open(PhDialogComponent, {
      width: '600px',
      //data: 'This text is passed into the dialog!'
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog closed: ${result}`);
      this.dialogResult = result;
      this.changed.emit(result);
    });
  }

}
