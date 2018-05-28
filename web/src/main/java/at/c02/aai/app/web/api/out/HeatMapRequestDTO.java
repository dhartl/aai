package at.c02.aai.app.web.api.out;

public class HeatMapRequestDTO {
	private DoctorFindRequestDTO doctorRequest;
	private int maxDistanceInMeter;
	/**
	 * Optional reference value the distances are normalized to
	 */
	private Integer intensityReference;

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

	public Integer getIntensityReference() {
		return intensityReference;
	}

	public void setIntensityReference(Integer intensityReference) {
		this.intensityReference = intensityReference;
	}


}
