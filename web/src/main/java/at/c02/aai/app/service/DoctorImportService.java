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
import at.c02.aai.app.db.entity.Insurance;
import at.c02.aai.app.db.entity.Speciality;
import at.c02.aai.app.db.repository.DoctorRepository;
import at.c02.aai.app.db.repository.InsuranceRepository;
import at.c02.aai.app.db.repository.SpecialityRepository;
import at.c02.aai.app.web.api.in.DoctorDTO;

@Service
@Transactional
public class DoctorImportService {

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private InsuranceRepository insuranceRepository;

	@Autowired
	private SpecialityRepository specialityRepository;

	public List<DoctorDTO> importDoctors(List<DoctorDTO> doctors) {
		InsuranceCache insuranceCache = new InsuranceCache(insuranceRepository);
		SpecialityCache specialityCache = new SpecialityCache(specialityRepository);
		return doctors.stream().map(doctorDto -> mapFromDTO(doctorDto, insuranceCache, specialityCache))
				.map(doctorRepository::save).map(this::mapToDTO).collect(Collectors.toList());
	}

	private Doctor mapFromDTO(DoctorDTO doctorDto, InsuranceCache insuranceCache, SpecialityCache specialityCache) {
		Doctor doctor = new Doctor();
		doctor.setDoctorId(doctorDto.getId());
		if (doctorDto.getInsurances() != null) {
			Set<Insurance> insurances = doctor.getInsurances();
			doctorDto.getInsurances().stream().map(insuranceCache::findOrCreate).filter(Objects::nonNull)
					.forEach(insurances::add);
		}
		if (doctorDto.getSpecialities() != null) {
			Set<Speciality> specialities = doctor.getSpecialities();
			doctorDto.getSpecialities().stream().map(specialityCache::findOrCreate).filter(Objects::nonNull)
					.forEach(specialities::add);
		}
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
	facility.setHoursTotal(FacilityUtils.calculateHoursTotal(hours));
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
