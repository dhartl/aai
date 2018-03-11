package at.c02.aai.app.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import at.c02.aai.app.db.entity.Facility;

public interface FacilityRepository extends JpaRepository<Facility, Long> {

}
