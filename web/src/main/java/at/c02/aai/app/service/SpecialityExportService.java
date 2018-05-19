package at.c02.aai.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.c02.aai.app.db.entity.Speciality;
import at.c02.aai.app.db.repository.SpecialityRepository;
import at.c02.aai.app.web.api.out.SpecialityOutDTO;

@Service
public class SpecialityExportService {

	@Autowired
	private SpecialityRepository specialityRepository;

	public List<SpecialityOutDTO> findSpecialities() {
		return specialityRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
	}

	private SpecialityOutDTO mapToDto(Speciality speciality) {
		return new SpecialityOutDTO(speciality.getId(), speciality.getName());
	}

}
