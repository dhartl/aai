package at.c02.aai.app.db.repository;

import java.util.List;

import at.c02.aai.app.web.api.out.DoctorFindRequestDTO;
import at.c02.aai.app.web.api.out.DoctorOutDTO;

public interface DoctorRepositoryCustom {

	List<DoctorOutDTO> findByRequest(DoctorFindRequestDTO request);
}
