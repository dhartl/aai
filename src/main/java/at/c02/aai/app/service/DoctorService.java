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
import at.c02.aai.app.web.api.DoctorDTO;

@Service
@Transactional
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    public DoctorDTO createDoctor(DoctorDTO doctorDto) {
	return mapToDTO(doctorRepository.save(mapFromDTO(doctorDto)));
    }

    public DoctorDTO getDoctor(Long doctorId) {
	return mapToDTO(doctorRepository.findById(doctorId).orElse(null));
    }

    public void deleteDoctor(Long doctorId) {
	doctorRepository.deleteById(doctorId);
    }

    public List<DoctorDTO> getAll() {
	return doctorRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private Doctor mapFromDTO(DoctorDTO doctorDto) {
	Doctor doctor = new Doctor();
	doctor.setDoctorId(doctorDto.getId());
	Facility facility = new Facility();
	facility.setTitle(doctorDto.getTitle());
	facility.setFacilityType(FacilityType.DOCTOR);
	facility.setStreet(doctorDto.getStreet());
	facility.setHouseNumber(doctorDto.getHouseNumber());
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
	doctorDto.setHouseNumber(facility.getHouseNumber());
	doctorDto.setZipCode(facility.getZipCode());
	doctorDto.setCity(facility.getCity());
	doctorDto.setGeoLat(facility.getGeoLat());
	doctorDto.setGeoLon(facility.getGeoLon());
	return doctorDto;
    }

}
