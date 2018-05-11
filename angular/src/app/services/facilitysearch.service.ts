import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Facility } from '../entities/facility';

const httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

@Injectable()
export class FacilitySearchService{

    constructor(private http:HttpClient){}
    
    getMockup()
    {
        return this
        .http
        .get<Facility[]>('http://localhost:4200/assets/mock-facilities.json');

      
    }

    tryAPI()
    {
        let url = "http://fortunecookieapi.com/v1/fortunes?limit=&skip=&page=";
        return this
        .http
        .get(url);
    }


    findById(id:number)
    {
        let url = "http://localhost:xxx"+id;
        
        return this
        .http
        .get<Facility>(url);    
    }

    getAll()
    {
        let url = "http://localhost:xxx";

        return this
        .http
        .get<Facility[]>(url);
    }
}