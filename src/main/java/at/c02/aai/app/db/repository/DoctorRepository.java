package at.c02.aai.app.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import at.c02.aai.app.db.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

}
