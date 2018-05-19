package at.c02.aai.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.c02.aai.app.db.repository.DoctorRepository;
import at.c02.aai.app.web.api.out.DoctorFindRequestDTO;
import at.c02.aai.app.web.api.out.DoctorOutDTO;

@Service
public class DoctorExportService {

	@Autowired
	private DoctorRepository doctorRepository;

	public List<DoctorOutDTO> findDoctors(DoctorFindRequestDTO request) {
		return doctorRepository.findByRequest(request);
	}

}
