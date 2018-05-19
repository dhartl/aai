package at.c02.aai.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.c02.aai.app.db.entity.Facility;
import at.c02.aai.app.db.entity.Pharmacy;
import at.c02.aai.app.db.repository.PharmacyRepository;
import at.c02.aai.app.web.api.out.PharmacyOutDTO;

@Service
public class PharmacyExportService {

	@Autowired
	private PharmacyRepository pharmacyRepository;

	public List<PharmacyOutDTO> findPharmacies() {
		return pharmacyRepository.findWithGeoCoordinates().stream().map(this::mapToDto).collect(Collectors.toList());
	}

	private PharmacyOutDTO mapToDto(Pharmacy pharmacy) {
		Facility facility = pharmacy.getFacility();
		return new PharmacyOutDTO(pharmacy.getId(), facility.getTitle(), facility.getGeoLat(), facility.getGeoLon());
	}

}
