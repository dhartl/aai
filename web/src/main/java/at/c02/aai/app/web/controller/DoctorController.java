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

import at.c02.aai.app.service.DoctorService;
import at.c02.aai.app.web.api.DoctorDTO;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PutMapping
    @ResponseBody
    public DoctorDTO create(@RequestBody DoctorDTO doctorDto) {
	return doctorService.createDoctor(doctorDto);
    }

    @DeleteMapping(path = "/{doctorId}")
    public ResponseEntity<Void> delete(@PathVariable(name = "doctorId", required = true) Long doctorId) {
	doctorService.deleteDoctor(doctorId);
	return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    @ResponseBody
    public List<DoctorDTO> getAll() {
	return doctorService.getAll();
    }
}
