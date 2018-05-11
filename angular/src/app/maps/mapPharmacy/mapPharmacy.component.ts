import { Component } from '@angular/core';
import { latLng, tileLayer } from 'leaflet';
import { FacilitySearchService } from '../../services/facilitysearch.service';

@Component({
  selector: 'mapPharmacy',
  templateUrl: 'mapPharmacy.component.html',
  styleUrls: ['mapPharmacy.component.css']
})
export class MapPharmacyComponent {

  constructor(private _CookieSearch: FacilitySearchService )
  {
     this._CookieSearch.tryAPI()
  }

  testData = {
    max: 8

  };

  

  options = {
    layers: [
      tileLayer('http://{s}.google.com/vt/lyrs=m&x={x}&y={y}&z={z}', {
        maxZoom: 20,
        subdomains: ['mt0', 'mt1', 'mt2', 'mt3'],
        detectRetina: true
      })
    ],
    zoom: 13,
    center: latLng([47.069241, 15.438473])
  };



}