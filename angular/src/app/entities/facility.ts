export interface Facility {
    
    title:string;
    street:string;
    zipCode:string;
    city:string;
    state:string;
    //lat statt geoLat
    lat: number;
    //lng statt geoLon
    lng: number;
    telephoneNumber: string;
    email: string;
    url: string;
    srcUrl:string;
    specialities: any;
    //hoursTotal:number;
    hours: any;
    insurances: any;
    //distance: number;
}
//facilityId:number;
    //facilityType:string;