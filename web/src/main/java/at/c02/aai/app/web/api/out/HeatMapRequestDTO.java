package at.c02.aai.app.web.api.out;

public class HeatMapRequestDTO {
	private DoctorFindRequestDTO doctorRequest;
	private int maxDistanceInMeter;

	public DoctorFindRequestDTO getDoctorRequest() {
		return doctorRequest;
	}

	public void setDoctorRequest(DoctorFindRequestDTO doctorRequest) {
		this.doctorRequest = doctorRequest;
	}

	public int getMaxDistanceInMeter() {
		return maxDistanceInMeter;
	}

	public void setMaxDistanceInMeter(int maxDistanceInMeter) {
		this.maxDistanceInMeter = maxDistanceInMeter;
	}

}
