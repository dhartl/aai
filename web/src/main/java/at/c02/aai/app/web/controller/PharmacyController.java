package at.c02.aai.app.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import at.c02.aai.app.service.PharmacyService;
import at.c02.aai.app.web.api.PharmacyDTO;

@Controller
@RequestMapping("/pharmacy")
public class PharmacyController {

    @Autowired
    private PharmacyService pharmacyService;

    @PutMapping
    @ResponseBody
    public PharmacyDTO create(@RequestBody PharmacyDTO pharmacyDto) {
	return pharmacyService.createPharmacy(pharmacyDto);
    }

    @DeleteMapping(path = "/{pharmacyId}")
    public ResponseEntity<Void> delete(@PathVariable(name = "pharmacyId", required = true) Long pharmacyId) {
	pharmacyService.deletePharmacy(pharmacyId);
	return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    @ResponseBody
    public List<PharmacyDTO> getAll() {
	return pharmacyService.getAll();
    }
}
