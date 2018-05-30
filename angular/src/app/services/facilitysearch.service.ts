import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map, tap, retry } from 'rxjs/operators';
import { Facility } from '../entities/facility';
import { Speciality } from '../entities/speciality';
import { Insurance } from '../entities/insurance';
import { Pharmacy } from '../entities/pharmacy';
import { DoctorRequest } from '../entities/doctorRequest';
import { Doctor} from '../entities/doctor';
import { HeatmapRequest } from '../entities/heatmapRequest';
import { HeatmapPoint } from '../entities/heatmapPoint';


const httpOptions = {
    headers: new HttpHeaders({ 
        'Content-Type': 'application/json' 
    })
  };
const httpPutOptions ={
    headers: new HttpHeaders({
        'Content-Type':'application/json',

    })
}

@Injectable()
export class FacilitySearchService{

    constructor(private http:HttpClient){}
    

    private handleError(error: HttpErrorResponse) {
        if (error.error instanceof ErrorEvent) {
          // A client-side or network error occurred. Handle it accordingly.
          console.error('An error occurred:', error.error.message);
        } else {
          // The backend returned an unsuccessful response code.
          // The response body may contain clues as to what went wrong,
          console.error(
            `Backend returned code ${error.status}, ` +
            `body was: ${error.error}`);
        }
        // return an observable with a user-facing error message
        return throwError(
          'Something bad happened; please try again later.');
      };
      
    
    tryAPI()
    {
        let url = "http://fortunecookieapi.com/v1/fortunes?limit=&skip=&page=";
        return this
        .http
        .get(url);
    }

    getStatesAsString()
    {
        let url = "http://localhost:9872/aai/state";
        return this.http.get(url,httpOptions).pipe(catchError(this.handleError));
    }

    getSpecialities()
    {
        let url = "http://localhost:9872/aai/speciality";
        return this.http.get<Speciality[]>(url,httpOptions).pipe(catchError(this.handleError));
    }

    getInsurances()
    {
        let url = "http://localhost:9872/aai/insurance";
        return this.http.get<Insurance[]>(url,httpOptions).pipe(catchError(this.handleError));
    }

    getPharmacies()
    {
        let url = "http://localhost:9872/aai/pharmacy"
        return this.http.get<Pharmacy[]>(url,httpOptions).pipe(catchError(this.handleError));
    }

    postDoctor(dr:DoctorRequest):Observable<Doctor[]>
    {
        let url = "http://localhost:9872/aai/doctor"
        return this.http.post<Doctor[]>(url,dr,httpOptions).pipe(catchError(this.handleError));
    }

    postHeatmap(hr:HeatmapRequest):Observable<HeatmapPoint[]>
    {
        let url = "http://localhost:9872/aai/heatmap"
        return this.http.post<HeatmapPoint[]>(url,hr,httpOptions).pipe(retry(3),catchError(this.handleError));
    }

   
}