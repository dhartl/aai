package at.c02.aai.app.db.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import at.c02.aai.app.db.entity.QDoctor;
import at.c02.aai.app.db.entity.QFacility;
import at.c02.aai.app.web.api.out.DoctorFindRequestDTO;
import at.c02.aai.app.web.api.out.DoctorOutDTO;

public class DoctorRepositoryCustomImpl implements DoctorRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<DoctorOutDTO> findByRequest(DoctorFindRequestDTO request) {
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		QDoctor doctor = QDoctor.doctor;
		QFacility facility = doctor.facility;

		BooleanExpression where = facility.geoLat.isNotNull().and(facility.geoLon.isNotNull());

		if (request.getInsuranceIds() != null && !request.getInsuranceIds().isEmpty()) {
			where = where.and(doctor.insurances.any().insuranceId.in(request.getInsuranceIds()));
		}

		if (request.getSpecialityIds() != null && !request.getSpecialityIds().isEmpty()) {
			where = where.and(doctor.specialities.any().specialityId.in(request.getSpecialityIds()));
		}
		if (request.getStates() != null && !request.getStates().isEmpty()) {
			where = where.and(doctor.facility.state.in(request.getStates()));
		}

		Map<String, Expression<?>> doctorOutDtoMapping = new HashMap<>();
		doctorOutDtoMapping.put("id", doctor.doctorId);
		doctorOutDtoMapping.put("name", facility.title);
		doctorOutDtoMapping.put("geoLat", facility.geoLat);
		doctorOutDtoMapping.put("geoLon", facility.geoLon);

		return queryFactory.select(
				Projections.bean(DoctorOutDTO.class, doctorOutDtoMapping))
				.from(doctor).join(doctor.facility).where(where).fetch();
	}

}
