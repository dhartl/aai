package at.c02.aai.app.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import at.c02.aai.app.db.entity.Speciality;
import at.c02.aai.app.db.repository.SpecialityRepository;

public class SpecialityCache {

	private SpecialityRepository specialityRepository;

	private Map<String, Speciality> specialities = new HashMap<>();

	public SpecialityCache(SpecialityRepository specialityRepository) {
		super();
		this.specialityRepository = specialityRepository;
		specialityRepository.findAll().forEach(this::addSpeciality);
	}

	public Speciality findOrCreate(String name) {
		String key = getKey(name);
		if (key != null) {
			return specialities.computeIfAbsent(key, mapKey -> createSpeciality(name));
		}
		return null;
	}

	private Speciality createSpeciality(String name) {
		Speciality speciality = new Speciality();
		speciality.setName(name);
		return specialityRepository.save(speciality);
	}

	private void addSpeciality(Speciality speciality) {
		String key = getKey(speciality.getName());
		if (key != null) {
			specialities.put(key, speciality);
		}
	}

	private String getKey(String name) {
		if (name == null) {
			return null;
		}
		return StringUtils.trimToNull(name.toLowerCase());
	}

}
