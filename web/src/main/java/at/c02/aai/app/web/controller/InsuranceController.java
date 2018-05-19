package at.c02.aai.app.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import at.c02.aai.app.service.InsuranceExportService;
import at.c02.aai.app.service.InsuranceImportService;
import at.c02.aai.app.web.api.in.InsuranceDTO;
import at.c02.aai.app.web.api.out.InsuranceOutDTO;

@Controller
@RequestMapping("/insurance")
public class InsuranceController {

	@Autowired
	private InsuranceImportService insuranceImportService;

	@Autowired
	private InsuranceExportService insuranceExportService;

	@PutMapping("/import")
	@ResponseBody
	public List<InsuranceDTO> importDoctors(@RequestBody List<InsuranceDTO> insurances) {
		return insuranceImportService.importInsurances(insurances);
	}

	@GetMapping
	@ResponseBody
	public List<InsuranceOutDTO> findInsurances() {
		return insuranceExportService.findInsurances();
	}
}
