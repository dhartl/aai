package at.c02.aai.app.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import at.c02.aai.app.service.PharmacyExportService;
import at.c02.aai.app.service.PharmacyImportService;
import at.c02.aai.app.web.api.in.PharmacyDTO;
import at.c02.aai.app.web.api.out.PharmacyOutDTO;

@Controller
@RequestMapping("/pharmacy")
public class PharmacyController {

	@Autowired
	private PharmacyImportService pharmacyImportService;

	@Autowired
	private PharmacyExportService pharmacyExportService;

	@PutMapping("/import")
	@ResponseBody
	public List<PharmacyDTO> importDoctors(@RequestBody List<PharmacyDTO> pharmacies) {
		return pharmacyImportService.importPharmacys(pharmacies);
	}

	@GetMapping
	@ResponseBody
	public List<PharmacyOutDTO> findPharmacies() {
		return pharmacyExportService.findPharmacies();
	}
}
