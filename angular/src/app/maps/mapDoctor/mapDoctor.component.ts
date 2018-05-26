import { Component, Input,  OnChanges, SimpleChange, OnInit, Output, EventEmitter } from '@angular/core';
import { latLng, tileLayer, icon, map, marker, markerClusterGroup, MarkerCluster, MarkerClusterGroupOptions, MarkerOptions } from 'leaflet';
import { Facility } from '../../entities/facility';
import { FacilitySearchService } from '../../services/facilitysearch.service';
import { HeatmapRequest } from '../../entities/heatmapRequest';
import { DoctorRequest } from '../../entities/doctorRequest';
import { Doctor } from '../../entities/doctor';
import { Pharmacy } from '../../entities/pharmacy';
import { HeatmapPoint } from '../../entities/heatmapPoint';
import 'leaflet.markercluster';
import * as L from 'leaflet';


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

  @Output() changed = new EventEmitter<DoctorRequest>();

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
  @Input()doctorRequest:DoctorRequest;

  ngOnChanges(changes: {[propKey: string]:SimpleChange}){
    this.getDataFromBackend(this.doctorRequest,this.heatmapRequest); 
    this.heatmapLayer.setData(this.heatmapData);
  }

  changeData()
  {
    this.getDataFromBackend(this.doctorRequest,this.heatmapRequest); 
    this.heatmapLayer.setData(this.heatmapData);
  }

  constructor(private _DoctorSearch: FacilitySearchService){
    
  

    this.getMarkerData();
   
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
   }
  pharmacies = {
    data: [<Pharmacy>{geoLat:47.076668, geoLon:15.821371,id:2,name:"Campus02"}]
  }
  doctors = {
    data: [<Doctor>{geoLat:47.076668, geoLon:15.421371, id:3, name:"Doctor Karate"}]
  }
  //47.076668, 15.421371


  // doctorRequest = <DoctorRequest>{
  //   insuranceIds: [],
  //   specialityIds: []

  // };
  heatmapRequest = <HeatmapRequest>{
    doctorRequest:this.doctorRequest,
    maxDistanceInMeter:2000
  };

  pharmacyIcon = L.icon({
    iconSize: [40,45],
    iconAnchor:[13,41],
    iconUrl: 'assets/pharmacy.png'
    //shadowUrl: 'leaf-shadow.png',
    // iconSize:     [38, 95], // size of the icon
    // shadowSize:   [50, 64], // size of the shadow
    // iconAnchor:   [22, 94], // point of the icon which will correspond to marker's location
    // shadowAnchor: [4, 62],  // the same for the shadow
    // popupAnchor:  [-3, -76] // point from which the popup should open relative to the iconAnchor
    });

    doctorIcon = L.icon({ 
      iconSize: [40,45],
      iconAnchor:[13,41],
      iconUrl: 'assets/medicine.png'
    })
 
  heatmapLayer = new HeatmapOverlay({
    radius: 20,
    blur: 4,
    gradient: {
     0: 'blue',
     .5: 'lime',
      1: 'red'
    },
    minOpacity: 0.1,
    maxOpacity: 300,
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
  markerClusterOptions: L.MarkerClusterGroupOptions;

  markerClusterReady(group: L.MarkerClusterGroup) {

		this.markerClusterGroup = group;

}
getMarkerData() {

  const data: any[] = [];

  // for(var doc of this.doctors.data)
  // {
  //  data.push(L.marker([doc.geoLat,doc.geoLon], {icon:this.doctorIcon}));
  // }

  // for(var pha of this.pharmacies.data)
  // {
  //  data.push(L.marker([pha.geoLat,pha.geoLon], {icon:this.pharmacyIcon}));
  // }
    

  this.markerClusterData = data;

}

  onMapReady(map: L.Map) {
    
   map.once('mousemove', (event: L.LeafletMouseEvent) =>{
    //this.getMarkerData();
    for(var doc of this.doctors.data)
    {
     L.marker([doc.geoLat,doc.geoLon], {icon:this.doctorIcon}).addTo(map);
    }
  
    for(var pha of this.pharmacies.data)
    {
     L.marker([pha.geoLat,pha.geoLon], {icon:this.pharmacyIcon}).addTo(map);
    }
   });
  //this.getMarkerData();

  //  //L.markerClusterGroup.bind(this.markerClusterData);
  for(var doc of this.doctors.data)
  {
   L.marker([doc.geoLat,doc.geoLon], {icon:this.doctorIcon}).addTo(map);
  }

  for(var pha of this.pharmacies.data)
  {
   L.marker([pha.geoLat,pha.geoLon], {icon:this.pharmacyIcon}).addTo(map);
  }
   
    map.on('mousemove', (event: L.LeafletMouseEvent) => {
      // this.heatmapData.data.push({
      //   geoLat: event.latlng.lat,
      //   geoLon: event.latlng.lng,
      //   intensity: 1
      // });
      L.marker([47.076668, 15.421371],{ icon:this.doctorIcon
        // icon:icon({
        //   iconSize: [25,41],
        //   iconAnchor:[13,41],
        //   iconUrl: 'leaflet/marker-icon.png',
        //   shadowUrl: 'leaflet/marker-shadow.png'
        // })
      }).addTo(map);

      
      //this.heatmapData.data = data;
      // this.heatmapLayer.setData(this.heatmapData);

    });
  }
}