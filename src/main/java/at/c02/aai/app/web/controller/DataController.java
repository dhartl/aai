package at.c02.aai.app.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import at.c02.aai.app.AaiConstants;
import at.c02.aai.app.service.FacilityDistanceService;

@Controller
@RequestMapping("/data")
public class DataController {

	@Autowired
	private FacilityDistanceService facilityDistanceService;

	@GetMapping("/distance")
	public ResponseEntity<Void> calculateDistances() {
		facilityDistanceService.calculateFacilityDistances(AaiConstants.MAX_FACILITY_DISTANCE_IN_METER);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
