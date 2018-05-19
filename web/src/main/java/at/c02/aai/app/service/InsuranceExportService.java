package at.c02.aai.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.c02.aai.app.db.entity.Insurance;
import at.c02.aai.app.db.repository.InsuranceRepository;
import at.c02.aai.app.web.api.out.InsuranceOutDTO;

@Service
public class InsuranceExportService {

	@Autowired
	private InsuranceRepository insuranceRepository;

	public List<InsuranceOutDTO> findInsurances() {
		return insuranceRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
	}

	private InsuranceOutDTO mapToDto(Insurance insurance) {
		return new InsuranceOutDTO(insurance.getId(), insurance.getName());
	}

}
