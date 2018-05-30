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
    specialityIds: [],
    states:[],
    },
    maxDistanceInMeter: 2000,
    }

  @Output() changed = new EventEmitter<HeatmapRequest>();
  

  constructor(public dialog: MatDialog) {
  
  }

  firstClick: boolean = true;
  
  ngOnInit() {
  }
  openDialog() {
    this.firstClick = false;
    let dialogRef = this.dialog.open(PhDialogComponent, {
      width: '600px',
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog closed: ${result}`);
      this.dialogResult = result;
      this.changed.emit(result);
    });
  }

}
