package at.c02.aai.app.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.c02.aai.app.db.entity.Speciality;
import at.c02.aai.app.db.entity.SpecialitySynonyme;
import at.c02.aai.app.db.repository.SpecialityRepository;
import at.c02.aai.app.web.api.in.SpecialityDTO;

@Service
@Transactional
public class SpecialityImportService {

    @Autowired
    private SpecialityRepository specialityRepository;

    public List<SpecialityDTO> importSpecialites(List<SpecialityDTO> specialities) {
	return specialities.stream().map(this::mapFromDTO).filter(speciality -> Objects.nonNull(speciality.getName()))
		.map(specialityRepository::save).map(this::mapToDTO).collect(Collectors.toList());
    }

    private Speciality mapFromDTO(SpecialityDTO specialityDto) {
	Speciality speciality = new Speciality();
	speciality.setName(StringUtils.trimToNull(specialityDto.getName()));
	specialityDto.getSynonymes().stream().map(this::mapFromString).forEach(speciality::addSynonyme);
	return speciality;
    }

    private SpecialitySynonyme mapFromString(String synonyme) {
	SpecialitySynonyme syn = new SpecialitySynonyme();
	syn.setName(synonyme);
	return syn;
    }

    private SpecialityDTO mapToDTO(Speciality speciality) {
	if (speciality == null) {
	    return null;
	}
	SpecialityDTO specialityDto = new SpecialityDTO();
	specialityDto.setName(speciality.getName());
	specialityDto.setSynonymes(
		speciality.getSynonymes().stream().map(SpecialitySynonyme::getName).collect(Collectors.toList()));
	return specialityDto;
    }
}
