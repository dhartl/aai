package at.c02.aai.app.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.c02.aai.app.db.entity.Insurance;
import at.c02.aai.app.db.repository.InsuranceRepository;
import at.c02.aai.app.web.api.in.InsuranceDTO;

@Service
@Transactional
public class InsuranceImportService {

	@Autowired
	private InsuranceRepository insuranceRepository;

	public List<InsuranceDTO> importInsurances(List<InsuranceDTO> insurances) {
		return insurances.stream().map(this::mapFromDTO).map(insuranceRepository::save).map(this::mapToDTO)
				.collect(Collectors.toList());
	}

	private Insurance mapFromDTO(InsuranceDTO insuranceDto) {
		Insurance insurance = new Insurance();
		insurance.setCode(insuranceDto.getCode());
		insurance.setName(insuranceDto.getName());
		return insurance;
	}

	private InsuranceDTO mapToDTO(Insurance insurance) {
		if (insurance == null) {
			return null;
		}
		InsuranceDTO insuranceDto = new InsuranceDTO();
		insuranceDto.setCode(insurance.getCode());
		insuranceDto.setName(insurance.getName());
		return insuranceDto;
	}
}
