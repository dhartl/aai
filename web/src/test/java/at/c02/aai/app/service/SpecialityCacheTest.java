package at.c02.aai.app.service;

import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import at.c02.aai.app.db.repository.SpecialityRepository;

public class SpecialityCacheTest {

    @Test
    public void testSplit() {
	SpecialityCache cache = new SpecialityCache(Mockito.mock(SpecialityRepository.class));
	List<String> specialities = cache.prepareSpeciality("alt u. jung.");
	System.out.println(specialities);
    }
}
