package at.c02.aai.app.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.c02.aai.app.db.entity.Facility;
import at.c02.aai.app.db.entity.FacilityType;
import at.c02.aai.app.db.entity.Pharmacy;
import at.c02.aai.app.db.repository.PharmacyRepository;
import at.c02.aai.app.web.api.PharmacyDTO;

@Service
@Transactional
public class PharmacyService {

    @Autowired
    private PharmacyRepository pharmacyRepository;

    public PharmacyDTO createPharmacy(PharmacyDTO pharmacyDto) {
	return mapToDTO(pharmacyRepository.save(mapFromDTO(pharmacyDto)));
    }

    public PharmacyDTO getPharmacy(Long pharmacyId) {
	return mapToDTO(pharmacyRepository.findById(pharmacyId).orElse(null));
    }

    public void deletePharmacy(Long pharmacyId) {
	pharmacyRepository.deleteById(pharmacyId);
    }

    public List<PharmacyDTO> getAll() {
	return pharmacyRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private Pharmacy mapFromDTO(PharmacyDTO pharmacyDto) {
	Pharmacy pharmacy = new Pharmacy();
	pharmacy.setPharmacyId(pharmacyDto.getId());
	Facility facility = new Facility();
	facility.setTitle(pharmacyDto.getTitle());
	facility.setFacilityType(FacilityType.PHARMACY);
	facility.setStreet(pharmacyDto.getStreet());
	facility.setHouseNumber(pharmacyDto.getHouseNumber());
	facility.setZipCode(pharmacyDto.getZipCode());
	facility.setCity(pharmacyDto.getCity());
	facility.setGeoLat(pharmacyDto.getGeoLat());
	facility.setGeoLon(pharmacyDto.getGeoLon());
	pharmacy.setFacility(facility);
	return pharmacy;
    }

    private PharmacyDTO mapToDTO(Pharmacy pharmacy) {
	if (pharmacy == null) {
	    return null;
	}
	PharmacyDTO pharmacyDto = new PharmacyDTO();
	pharmacyDto.setId(pharmacy.getPharmacyId());
	Facility facility = pharmacy.getFacility();
	pharmacyDto.setTitle(facility.getTitle());
	pharmacyDto.setStreet(facility.getStreet());
	pharmacyDto.setHouseNumber(facility.getHouseNumber());
	pharmacyDto.setZipCode(facility.getZipCode());
	pharmacyDto.setCity(facility.getCity());
	pharmacyDto.setGeoLat(facility.getGeoLat());
	pharmacyDto.setGeoLon(facility.getGeoLon());
	return pharmacyDto;
    }

}
