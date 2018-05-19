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

import at.c02.aai.app.service.bean.DistanceBean;
import at.c02.aai.app.service.bean.HasGeoCoordinates;

@Service
public class DistanceService {

	@Autowired
	private SpatialContext spatialContext;

	public List<DistanceBean> createDistances(List<? extends HasGeoCoordinates> facilitiesFrom,
			List<? extends HasGeoCoordinates> facilitiesTo, int maxDistanceInMeter) {

		List<DistanceBean> distances = new ArrayList<>();

		for (HasGeoCoordinates from : facilitiesFrom) {
			if (!HasGeoCoordinateUtils.hasCoordinates(from)) {
				continue;
			}
			Point fromPoint = getPointFromGeoCoordinate(from.getGeoLat(), from.getGeoLon());
			DistanceBean distanceBean = new DistanceBean(from.getGeoLat(), from.getGeoLon(), 0);
			for (HasGeoCoordinates to : facilitiesTo) {
				if (!Objects.equals(from, to) && HasGeoCoordinateUtils.hasCoordinates(to)) {
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
