package at.c02.aai.app.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import at.c02.aai.app.db.entity.Speciality;
import at.c02.aai.app.db.repository.SpecialityRepository;

public class SpecialityCache {

    private SpecialityRepository specialityRepository;

    private Map<String, Speciality> specialities = new HashMap<>();

    private List<String> wordsToReplace = Arrays.asList("Arzt", "Ärztin", "Facharzt", "Fachärztin", "für");
    private List<String> splitWords = Arrays.asList("/", "ZF:", "ZF:");

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
	return StringUtils.trimToNull(name.toLowerCase().replaceAll("[\\.,;:]", ""));
    }

    public List<String> prepareSpeciality(String speciality) {
	String specialityCleaned = speciality.replaceAll("u\\.", "und").replaceAll("\\.", "");

	List<String> currentList = Arrays.asList(specialityCleaned);
	for (String word : splitWords) {
	    List<String> newList = new ArrayList<>();
	    for (String sp : currentList) {
		newList.addAll(Arrays.asList(StringUtils.splitByWholeSeparator(sp, word)));
	    }
	    currentList = newList;
	}

	return currentList.stream().map(sp -> {
	    String spRet = sp;
	    for (String word : wordsToReplace) {
		spRet = StringUtils.trimToEmpty(StringUtils.remove(spRet, word));
	    }
	    return StringUtils.trimToNull(spRet);
	}).filter(Objects::nonNull).collect(Collectors.toList());
    }

}
