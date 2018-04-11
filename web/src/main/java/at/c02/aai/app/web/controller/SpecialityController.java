package at.c02.aai.app.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import at.c02.aai.app.service.SpecialityImportService;
import at.c02.aai.app.web.api.in.SpecialityDTO;

@Controller
@RequestMapping("/speciality")
public class SpecialityController {

    @Autowired
    private SpecialityImportService specialityImportService;

    @PutMapping("/import")
    @ResponseBody
    public List<SpecialityDTO> importDoctors(@RequestBody List<SpecialityDTO> specialitys) {
	return specialityImportService.importSpecialites(specialitys);
    }
}
