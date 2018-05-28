package at.c02.aai.app.service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.c02.aai.app.db.entity.Facility;
import at.c02.aai.app.db.entity.FacilityType;
import at.c02.aai.app.db.entity.Hours;
import at.c02.aai.app.db.entity.Pharmacy;
import at.c02.aai.app.db.repository.PharmacyRepository;
import at.c02.aai.app.web.api.in.PharmacyDTO;

@Service
@Transactional
public class PharmacyImportService {

    @Autowired
    private PharmacyRepository PharmacyRepository;

    public List<PharmacyDTO> importPharmacys(List<PharmacyDTO> pharmacys) {
	return pharmacys.stream().map(this::mapFromDTO).map(PharmacyRepository::save).map(this::mapToDTO)
		.collect(Collectors.toList());
    }

    private Pharmacy mapFromDTO(PharmacyDTO pharmacyDto) {
	Pharmacy pharmacy = new Pharmacy();
	pharmacy.setPharmacyId(pharmacyDto.getId());
	Facility facility = new Facility();
	facility.setTitle(StringUtils.trimToNull(pharmacyDto.getTitle()));
	facility.setFacilityType(FacilityType.PHARMACY);
	facility.setStreet(StringUtils.trimToNull(pharmacyDto.getStreet()));
	facility.setZipCode(StringUtils.trimToNull(pharmacyDto.getZipCode()));
	facility.setCity(StringUtils.trimToNull(pharmacyDto.getCity()));
	facility.setState(StringUtils.trimToNull(pharmacyDto.getState()));
	facility.setGeoLat(pharmacyDto.getGeoLat());
	facility.setGeoLon(pharmacyDto.getGeoLon());
	facility.setTelephoneNr(StringUtils.trimToNull(pharmacyDto.getTelephoneNumber()));
	facility.setEmail(StringUtils.trimToNull(pharmacyDto.getEmail()));
	facility.setUrl(StringUtils.trimToNull(pharmacyDto.getUrl()));
	facility.setSrcUrl(StringUtils.trimToNull(pharmacyDto.getSrcUrl()));
	Set<Hours> hours = facility.getHours();
	pharmacyDto.getHours().stream().map(FacilityUtils::mapFromHoursDto).filter(Objects::nonNull).map(hour -> {
	    hour.setFacility(facility);
	    return hour;
	}).forEach(hours::add);
	facility.setHoursTotal(FacilityUtils.calculateHoursTotal(hours));
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
	pharmacyDto.setZipCode(facility.getZipCode());
	pharmacyDto.setCity(facility.getCity());
	pharmacyDto.setState(facility.getState());
	pharmacyDto.setGeoLat(facility.getGeoLat());
	pharmacyDto.setGeoLon(facility.getGeoLon());
	pharmacyDto.setTelephoneNumber(facility.getTelephoneNr());
	pharmacyDto.setEmail(facility.getEmail());
	pharmacyDto.setUrl(facility.getUrl());
	pharmacyDto.setSrcUrl(facility.getSrcUrl());
	pharmacyDto
		.setHours(facility.getHours().stream().map(FacilityUtils::mapToHoursDto).collect(Collectors.toList()));
	return pharmacyDto;
	}
}
