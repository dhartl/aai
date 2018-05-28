package at.c02.aai.app.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import at.c02.aai.app.db.entity.Speciality;

public interface SpecialityRepository extends JpaRepository<Speciality, Long> {

    @EntityGraph(attributePaths = { "synonymes" })
    @Query("SELECT sp from Speciality sp")
    List<Speciality> findAllWithSynonymes();

}
