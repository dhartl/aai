package at.c02.aai.app.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import at.c02.aai.app.db.entity.Insurance;
import at.c02.aai.app.db.repository.InsuranceRepository;

public class InsuranceCache {
	private Map<String, Insurance> insurances = new HashMap<>();

	public InsuranceCache(InsuranceRepository insuranceRepository) {
		super();
		insuranceRepository.findAll().forEach(this::addInsurance);
	}

	public Insurance findOrCreate(String code) {
		String key = getKey(code);
		if (key != null) {
			return insurances.get(key);
		}
		return null;
	}

	private void addInsurance(Insurance insurance) {
		String key = getKey(insurance.getName());
		if (key != null) {
			insurances.put(key, insurance);
		}
	}

	private String getKey(String name) {
		if (name == null) {
			return null;
		}
		return StringUtils.trimToNull(name.toLowerCase());
	}
}
