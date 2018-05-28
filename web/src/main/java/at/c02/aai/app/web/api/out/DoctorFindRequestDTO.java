package at.c02.aai.app.web.api.out;

import java.util.ArrayList;
import java.util.List;

public class DoctorFindRequestDTO {
	private List<Long> specialityIds = new ArrayList<>();
	private List<Long> insuranceIds = new ArrayList<>();
	private List<String> states = new ArrayList<>();

	public List<Long> getSpecialityIds() {
		return specialityIds;
	}

	public void setSpecialityIds(List<Long> specialityIds) {
		this.specialityIds = specialityIds;
	}

	public List<Long> getInsuranceIds() {
		return insuranceIds;
	}

	public void setInsuranceIds(List<Long> insuranceIds) {
		this.insuranceIds = insuranceIds;
	}

	public List<String> getStates() {
		return states;
	}

	public void setStates(List<String> states) {
		this.states = states;
	}

	@Override
	public String toString() {
		return "DoctorFindRequestDTO [specialityIds=" + specialityIds + ", insuranceIds=" + insuranceIds + ", states="
				+ states + "]";
	}


}
