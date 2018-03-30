package at.c02.aai.app.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.c02.aai.app.db.entity.Doctor;
import at.c02.aai.app.db.entity.Facility;
import at.c02.aai.app.db.entity.FacilityType;
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
		facility.setTitle(doctorDto.getTitle());
		facility.setFacilityType(FacilityType.DOCTOR);
		facility.setStreet(doctorDto.getStreet());
		facility.setZipCode(doctorDto.getZipCode());
		facility.setCity(doctorDto.getCity());
		facility.setGeoLat(doctorDto.getGeoLat());
		facility.setGeoLon(doctorDto.getGeoLon());
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
		doctorDto.setGeoLat(facility.getGeoLat());
		doctorDto.setGeoLon(facility.getGeoLon());
		return doctorDto;
	}
}
