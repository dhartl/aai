package at.c02.aai.app.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import at.c02.aai.app.db.entity.Facility;
import at.c02.aai.app.db.entity.FacilityType;

public interface FacilityRepository extends JpaRepository<Facility, Long> {

	List<Facility> findByFacilityType(FacilityType doctor);

}
