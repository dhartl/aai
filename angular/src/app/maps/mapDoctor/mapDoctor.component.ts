import { Component } from '@angular/core';
import { latLng, tileLayer, icon, map } from 'leaflet';
import { Facility } from '../../entities/facility';
import { FacilitySearchService } from '../../services/facilitysearch.service';

declare var L;
declare var HeatmapOverlay;

@Component({
  selector: 'mapDoctor',
  templateUrl: 'mapDoctor.component.html',
  styleUrls: ['./mapDoctor.component.css']
})
export class MapDoctorComponent {

  constructor(private _DoctorSearch: FacilitySearchService){
    
         this.heatmapLayer.setData(this.data);
     
  };
  data = {
    data: [{"lat": 47.0693388, "lng": 15.4486467, "count":40},{"lat": 47.0693388, "lng": 15.4486467, "count":40},{"lat": 47.0693388, "lng": 15.4486467, "count":40},{"lat": 47.0693388, "lng": 15.4486467, "count":40},{"lat": 47.0693388, "lng": 15.4486467, "count":40},{"lat": 47.0693388, "lng": 15.4486467, "count":40},{"lat": 47.0693388, "lng": 15.4486467, "count":40},{"lat": 47.0693388, "lng": 15.4486467, "count":40},{"lat": 47.0693388, "lng": 15.4486467, "count":40},{"lat": 47.0693318, "lng": 15.4483467, "count":40},{"lat": 47.0693288, "lng": 15.4486457, "count":40},{"lat": 47.0693358, "lng": 15.4485467, "count":40},{"lat": 47.0693688, "lng": 15.4486467, "count":40},{"lat": 47.0733727, "lng": 15.4552345, "count":20 },{"lat": 47.5052428, "lng": 15.4504397, "count":10}]
  };



  doctors:Facility[];
 
  heatmapLayer = new HeatmapOverlay({
    radius: 25,
    blur: 0.75,
    gradient: {
     0.4: 'blue',
     0.65: 'lime',
      1.0: 'red'
    },
    minOpacity: 0.1,
    maxOpacity: 0.8,
    latField: 'lat',
    lngField: 'lng',
    valueField: 'count'
    
  })

  options = {
    layers: [
      L.tileLayer('http://{s}.google.com/vt/lyrs=m&x={x}&y={y}&z={z}', {
        maxZoom: 20,
        subdomains: ['mt0', 'mt1', 'mt2', 'mt3'],
        detectRetina: true
      }),
      this.heatmapLayer
    ],
    zoom: 13,
    center: L.latLng([47.076668, 15.421371 ])
  };
  onMapReady(map: L.Map) {
  
    map.on('mousemove', (event: L.LeafletMouseEvent) => {
      this.data.data.push({
        lat: event.latlng.lat,
        lng: event.latlng.lng,
        count: 1
      });
      L.marker([47.076668, 15.421371]).addTo(map);
    
      
    });
  }
}