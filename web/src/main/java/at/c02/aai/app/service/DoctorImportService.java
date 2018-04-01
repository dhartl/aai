package at.c02.aai.app.service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.c02.aai.app.db.entity.Doctor;
import at.c02.aai.app.db.entity.Facility;
import at.c02.aai.app.db.entity.FacilityType;
import at.c02.aai.app.db.entity.Hours;
import at.c02.aai.app.db.repository.DoctorRepository;
import at.c02.aai.app.web.api.in.DoctorDTO;

@Service
@Transactional
public class DoctorImportService {

	@Autowired
	private DoctorRepository doctorRepository;

	public List<DoctorDTO> importDoctors(List<DoctorDTO> doctors) {
		return doctors.stream().map(this::mapFromDTO).map(doctorRepository::save).map(this::mapToDTO)
				.collect(Collectors.toList());
	}

	private Doctor mapFromDTO(DoctorDTO doctorDto) {
		Doctor doctor = new Doctor();
		doctor.setDoctorId(doctorDto.getId());
		Facility facility = new Facility();
		facility.setTitle(StringUtils.trimToNull(doctorDto.getTitle()));
		facility.setFacilityType(FacilityType.DOCTOR);
		facility.setStreet(StringUtils.trimToNull(doctorDto.getStreet()));
		facility.setZipCode(StringUtils.trimToNull(doctorDto.getZipCode()));
		facility.setCity(StringUtils.trimToNull(doctorDto.getCity()));
		facility.setState(StringUtils.trimToNull(doctorDto.getState()));
		facility.setGeoLat(doctorDto.getGeoLat());
		facility.setGeoLon(doctorDto.getGeoLon());
		facility.setTelephoneNr(StringUtils.trimToNull(doctorDto.getTelephoneNumber()));
		facility.setEmail(StringUtils.trimToNull(doctorDto.getEmail()));
		facility.setUrl(StringUtils.trimToNull(doctorDto.getUrl()));
		facility.setSrcUrl(StringUtils.trimToNull(doctorDto.getSrcUrl()));
		Set<Hours> hours = facility.getHours();
		doctorDto.getHours().stream().map(FacilityUtils::mapFromHoursDto).filter(Objects::nonNull).map(hour -> {
			hour.setFacility(facility);
			return hour;
		}).forEach(hours::add);
		doctor.setFacility(facility);
		return doctor;
	}

	private DoctorDTO mapToDTO(Doctor doctor) {
		if (doctor == null) {
			return null;
		}
		DoctorDTO doctorDto = new DoctorDTO();
		doctorDto.setId(doctor.getDoctorId());
		Facility facility = doctor.getFacility();
		doctorDto.setTitle(facility.getTitle());
		doctorDto.setStreet(facility.getStreet());
		doctorDto.setZipCode(facility.getZipCode());
		doctorDto.setCity(facility.getCity());
		doctorDto.setState(facility.getState());
		doctorDto.setGeoLat(facility.getGeoLat());
		doctorDto.setGeoLon(facility.getGeoLon());
		doctorDto.setTelephoneNumber(facility.getTelephoneNr());
		doctorDto.setEmail(facility.getEmail());
		doctorDto.setUrl(facility.getUrl());
		doctorDto.setSrcUrl(facility.getSrcUrl());
		doctorDto.setHours(facility.getHours().stream().map(FacilityUtils::mapToHoursDto).collect(Collectors.toList()));
		return doctorDto;
	}
}
