import { DoctorRequest } from "./doctorRequest";

export interface HeatmapRequest {
    doctorRequest:DoctorRequest;
    maxDistanceInMeter:number;
}