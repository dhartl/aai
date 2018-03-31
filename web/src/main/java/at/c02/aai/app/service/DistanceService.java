package at.c02.aai.app.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.distance.DistanceUtils;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.impl.PointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.c02.aai.app.db.entity.Facility;
import at.c02.aai.app.service.bean.DistanceBean;

@Service
public class DistanceService {

	@Autowired
	private SpatialContext spatialContext;

	public List<DistanceBean> createDistances(List<Facility> facilitiesFrom, List<Facility> facilitiesTo,
			int maxDistanceInMeter) {

		List<DistanceBean> distances = new ArrayList<>();

		for (Facility from : facilitiesFrom) {
			if (!FacilityUtils.hasCoordinates(from)) {
				continue;
			}
			Point fromPoint = getPointFromGeoCoordinate(from.getGeoLat(), from.getGeoLon());
			DistanceBean distanceBean = new DistanceBean(from.getGeoLat(), from.getGeoLon(), 0);
			for (Facility to : facilitiesTo) {
				if (!Objects.equals(from, to) && FacilityUtils.hasCoordinates(to)) {
					Point toPoint = getPointFromGeoCoordinate(to.getGeoLat(), to.getGeoLon());
					double distance = calculateDistanceBetweenPoints(fromPoint, toPoint);
					if (distance <= maxDistanceInMeter) {
						distanceBean.setCount(distanceBean.getCount() + 1);
					}
				}
			}
			distances.add(distanceBean);
		}
		return distances;
	}

	private double calculateDistanceBetweenPoints(Point fromPoint, Point toPoint) {
		double distanceInDeg = spatialContext.calcDistance(fromPoint, toPoint);
		double distance = DistanceUtils.degrees2Dist(distanceInDeg, DistanceUtils.EARTH_MEAN_RADIUS_KM * 1000);
		return distance;
	}

	private Point getPointFromGeoCoordinate(BigDecimal geoLat, BigDecimal geoLon) {
		return new PointImpl(geoLon.doubleValue(), geoLat.doubleValue(), spatialContext);
	}
}
