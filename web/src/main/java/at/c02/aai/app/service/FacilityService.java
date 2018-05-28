package at.c02.aai.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.c02.aai.app.db.repository.FacilityRepository;

@Service
public class FacilityService {

	@Autowired
	private FacilityRepository facilityRepository;

	public List<String> findAllStates() {
		return facilityRepository.findAllStates();
	}
}
