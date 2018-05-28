package at.c02.aai.app.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import at.c02.aai.app.db.entity.Hours;

public interface HoursRepository extends JpaRepository<Hours, Long> {

}
