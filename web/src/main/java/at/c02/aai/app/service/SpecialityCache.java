package at.c02.aai.app.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.FuzzyScore;

import at.c02.aai.app.db.entity.Speciality;
import at.c02.aai.app.db.entity.SpecialitySynonyme;
import at.c02.aai.app.db.repository.SpecialityRepository;

public class SpecialityCache {


    private Map<String, Speciality> specialities = new HashMap<>();

    private List<String> wordsToReplace = Arrays.asList("Arzt", "Ärztin", "Facharzt", "Fachärztin", "für", "-");
    private List<String> splitWords = Arrays.asList("/", "ZF:", "ZF:", ";", ",");

    public SpecialityCache(SpecialityRepository specialityRepository) {
	super();
	specialityRepository.findAllWithSynonymes().forEach(this::addSpeciality);
    }

    public Speciality findSpeciality(String name) {
	FuzzyScore fuzzyScore = new FuzzyScore(Locale.GERMAN);
	String key = getKey(name);
	String specKey = specialities.keySet().stream()
		.map(spec -> new SpecScore(spec, fuzzyScore.fuzzyScore(spec, key)))
		.sorted(Comparator.comparing(SpecScore::getScore).reversed()).map(SpecScore::getKey).findFirst()
		.orElse(null);
	return specialities.get(specKey);
    }


    private void addSpeciality(Speciality speciality) {
	String key = getKey(speciality.getName());
	if (key != null) {
	    specialities.put(key, speciality);
	}
	for (SpecialitySynonyme synonyme : speciality.getSynonymes()) {
	    key = getKey(synonyme.getName());
	    if (key != null) {
		specialities.put(key, speciality);
	    }
	}
    }

    private String getKey(String name) {
	if (name == null) {
	    return null;
	}
	return StringUtils.trimToNull(name.toLowerCase().replaceAll("[\\.,;:]", ""));
    }

    public List<String> prepareSpeciality(String speciality) {

	List<String> currentList = Arrays.asList(speciality);
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

    private static final class SpecScore {
	private String key;
	private int score;

	public SpecScore(String key, int score) {
	    super();
	    this.key = key;
	    this.score = score;
	}

	public String getKey() {
	    return key;
	}

	public int getScore() {
	    return score;
	}

	@Override
	public String toString() {
	    return "SpecScore [key=" + key + ", score=" + score + "]";
	}

    }

}
