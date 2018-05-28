package at.c02.aai.app.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import at.c02.aai.app.service.DoctorExportService;
import at.c02.aai.app.service.DoctorImportService;
import at.c02.aai.app.web.api.in.DoctorDTO;
import at.c02.aai.app.web.api.out.DoctorFindRequestDTO;
import at.c02.aai.app.web.api.out.DoctorOutDTO;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

	@Autowired
	private DoctorImportService doctorImportService;

	@Autowired
	private DoctorExportService doctorExportService;

	@PutMapping("/import")
	@ResponseBody
	public List<DoctorDTO> importDoctors(@RequestBody List<DoctorDTO> doctors) {
		return doctorImportService.importDoctors(doctors);
	}

	@PostMapping
	@ResponseBody
	public List<DoctorOutDTO> findDoctors(@RequestBody DoctorFindRequestDTO request) {
		return doctorExportService.findDoctors(request);
	}
}
