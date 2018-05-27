import { Component, Input,  OnChanges, SimpleChange, OnInit, Output, EventEmitter } from '@angular/core';
import { latLng, tileLayer, icon, map } from 'leaflet';
import { Facility } from '../../entities/facility';
import { FacilitySearchService } from '../../services/facilitysearch.service';
import { HeatmapRequest } from '../../entities/heatmapRequest';
import { DoctorRequest } from '../../entities/doctorRequest';
import { Doctor } from '../../entities/doctor';
import { Pharmacy } from '../../entities/pharmacy';
import { HeatmapPoint } from '../../entities/heatmapPoint';
import { LeafletMarkerClusterModule } from '@asymmetrik/ngx-leaflet-markercluster';
import * as L from 'leaflet';
import 'leaflet.markercluster';


declare var HeatmapOverlay;
var data : HeatmapPoint[] = [{ geoLat:23.2,geoLon:23.434, intensity:3}];
var heatmapData;
var heatmapLayer;
declare var markers;



@Component({
  selector: 'mapDoctor',
  templateUrl: 'mapDoctor.component.html',
  styleUrls: ['./mapDoctor.component.css']
})
export class MapDoctorComponent implements OnInit, OnChanges {

  @Output() changed = new EventEmitter<HeatmapRequest>();

  ngOnInit(): void {
    this.doctorRequest = {
      insuranceIds : [9],
      specialityIds: []
    }
    this.heatmapRequest = {
      doctorRequest: this.doctorRequest,
      maxDistanceInMeter:2000
    }
    this.getDataFromBackend(this.doctorRequest,this.heatmapRequest);
    this.heatmapLayer.setData(this.heatmapData);

  }

  @Input()heatmapRequest:HeatmapRequest;

  ngOnChanges(changes: {[propKey: string]:SimpleChange}){
    this.getDataFromBackend(this.heatmapRequest.doctorRequest,this.heatmapRequest); 
    this.heatmapLayer.setData(this.heatmapData);
  }

  changeData()
  {
    this.doctorRequest = this.heatmapRequest.doctorRequest;
    this.getDataFromBackend(this.doctorRequest,this.heatmapRequest); 
    this.heatmapLayer.setData(this.heatmapData);
    this.setMarkerData();

  }

  constructor(private _DoctorSearch: FacilitySearchService){
     };

  getDataFromBackend(dr:DoctorRequest, hr:HeatmapRequest){
    this._DoctorSearch.postHeatmap(this.heatmapRequest).subscribe((data) => {
      this.heatmapData.data = data;
    });

    this._DoctorSearch.getPharmacies().subscribe((data) =>
    {
      this.pharmacies.data = data;
    })
    this._DoctorSearch.postDoctor(dr).subscribe((data) =>
    {
      this.doctors.data = data;
    })
  }
  
   heatmapData = {
     data: [<HeatmapPoint>{ geoLat:23.2,geoLon:23.434, intensity:3}]
   };
  pharmacies = {
    data: [<Pharmacy>{geoLat:47.076668, geoLon:15.821371,id:2,name:"Campus02"}]
  };
  doctors = {
    data: [<Doctor>{geoLat:47.076668, geoLon:15.421371, id:3, name:"Doctor Karate"}]
  };
  
  doctorRequest = <DoctorRequest>{
    insuranceIds: [],
    specialityIds: []
  };

  pharmacyIcon = L.icon({
    iconSize: [40,45],
    iconAnchor:[13,41],
    iconUrl: 'assets/pharmacy.png'
    });

    doctorIcon = L.icon({ 
      iconSize: [40,45],
      iconAnchor:[13,41],
      iconUrl: 'assets/medicine.png'
    });
 
    layerdoctors:L.Layer;

  heatmapLayer = new HeatmapOverlay({
    radius: 12,
    blur: 0.75,
    gradient: {
     0: 'blue',
     .5: 'lime',
      1: 'red'
    },
    minOpacity: 0.1,
    maxOpacity: 0.8,
    latField: 'geoLat',
    lngField: 'geoLon',
    valueField: 'intensity',
    
  })

  options = {
    layers: [
      L.tileLayer('https://maps{s}.wien.gv.at/basemap/geolandbasemap/normal/google3857/{z}/{y}/{x}.{format}', {
        maxZoom: 20,
        attribution: 'Datenquelle: <a href="https://www.basemap.at">basemap.at</a>',
	      subdomains: ["", "1", "2", "3", "4"],
	      format: 'png',
	      bounds: [[46.35877, 8.782379], [49.037872, 17.189532]],
        //subdomains: ['mt0', 'mt1', 'mt2', 'mt3'],
        detectRetina: true
      }),
      this.heatmapLayer
    ],
    zoom: 13,
    center: L.latLng([47.076668, 15.421371 ])
  };

  markerClusterGroup: L.MarkerClusterGroup;
	markerClusterData: any[] = [];
  markerClusterOptions: L.MarkerClusterGroupOptions ={
    spiderfyOnMaxZoom:false,
    disableClusteringAtZoom: 16,
    polygonOptions: {
      color: '#2d84c8',
      weight: 4,
      opacity: 1,
      fillOpacity: 0.5
    },
    // spiderfyOnMaxZoom: true,
    // showCoverageOnHover: false
  };

  markerClusterReady(group: L.MarkerClusterGroup) {

		this.markerClusterGroup = group;

}
setMarkerData() {

  const data: any[] = [];

  for(var doc of this.doctors.data)
  {
   data.push(L.marker([doc.geoLat,doc.geoLon], {icon:this.doctorIcon}));
  }

  for(var pha of this.pharmacies.data)
  {
   data.push(L.marker([pha.geoLat,pha.geoLon], {icon:this.pharmacyIcon}));
  }
    

  this.markerClusterData = data;

}


onMapReady(map: L.Map) {
    
  // this.setMarkerData();
  this.heatmapLayer.setData(this.heatmapData);


   map.once('mousemove', (event: L.LeafletMouseEvent) =>{

//  
    
    this.heatmapLayer.setData(this.heatmapData);

   });
  
   
  
   
    map.on('mousemove', (event: L.LeafletMouseEvent) => {
      
    });
  }
}