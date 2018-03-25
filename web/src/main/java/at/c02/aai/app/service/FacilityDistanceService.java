package at.c02.aai.app.service;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.distance.DistanceUtils;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.impl.PointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.c02.aai.app.db.entity.Facility;
import at.c02.aai.app.db.entity.FacilityDistance;
import at.c02.aai.app.db.entity.FacilityType;
import at.c02.aai.app.db.repository.FacilityDistanceRepository;
import at.c02.aai.app.db.repository.FacilityRepository;

@Service
@Transactional
public class FacilityDistanceService {

	@Autowired
	private FacilityRepository facilityRepository;

	@Autowired
	private FacilityDistanceRepository facilityDistanceRepository;

	@Autowired
	private SpatialContext spatialContext;

	/**
	 * calculates the distances between doctors and pharmacys, saving them in
	 * facilitydistance. only distances smaller than maxDistanceInMeter are saved
	 * 
	 * @param maxDistanceInMeter
	 */
	public void calculateFacilityDistances(int maxDistanceInMeter) {
		facilityDistanceRepository.deleteAll();
		List<Facility> doctors = facilityRepository.findByFacilityType(FacilityType.DOCTOR);
		List<Facility> pharmacies = facilityRepository.findByFacilityType(FacilityType.PHARMACY);

		for (Facility pharmacy : pharmacies) {
			Point pharmacyPoint = getPointFromGeoCoordinate(pharmacy.getGeoLat(), pharmacy.getGeoLon());
			for (Facility doctor : doctors) {
				Point doctorPoint = getPointFromGeoCoordinate(doctor.getGeoLat(), doctor.getGeoLon());
				double distanceInDeg = spatialContext.calcDistance(pharmacyPoint, doctorPoint);
				double distance = DistanceUtils.degrees2Dist(distanceInDeg,
						DistanceUtils.EARTH_MEAN_RADIUS_KM * 1000);
				if (distance <= maxDistanceInMeter) {
					createFacilityDistance(pharmacy, doctor, distance);
				}
			}
		}

	}

	private void createFacilityDistance(Facility pharmacy, Facility doctor, double distance) {
		FacilityDistance facilityDistance = new FacilityDistance();
		facilityDistance.setStart(pharmacy);
		facilityDistance.setEnd(doctor);
		facilityDistance.setDistance((int) distance);
		facilityDistanceRepository.save(facilityDistance);
	}

	private Point getPointFromGeoCoordinate(BigDecimal geoLat, BigDecimal geoLon) {
		return new PointImpl(geoLon.doubleValue(), geoLat.doubleValue(), spatialContext);
	}
}
