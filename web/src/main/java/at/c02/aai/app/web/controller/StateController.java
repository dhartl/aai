package at.c02.aai.app.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import at.c02.aai.app.service.FacilityService;

@Controller
@RequestMapping("/state")
public class StateController {

	@Autowired
	private FacilityService facilityService;

	@GetMapping
	@ResponseBody
	public List<String> findAllStates() {
		return facilityService.findAllStates();
	}
}
