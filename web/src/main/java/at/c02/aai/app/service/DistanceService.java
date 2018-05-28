package at.c02.aai.app.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.index.SpatialIndex;
import org.locationtech.jts.index.strtree.STRtree;
import org.locationtech.jts.util.GeometricShapeFactory;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.distance.DistanceUtils;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.impl.PointImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.c02.aai.app.service.bean.DistanceBean;
import at.c02.aai.app.service.bean.HasGeoCoordinates;

@Service
public class DistanceService {

	private static final Logger logger = LoggerFactory.getLogger(DistanceService.class);


	private SpatialContext spatialContext;

	@Autowired
	public void setSpatialContext(SpatialContext spatialContext) {
		this.spatialContext = spatialContext;
	}

	public List<DistanceBean> createDistances(List<? extends HasGeoCoordinates> facilitiesFrom,
			List<? extends HasGeoCoordinates> facilitiesTo, int maxDistanceInMeter) {
		logger.debug("creating distances from {} locations to {} locations; distance {}m", facilitiesFrom.size(),
				facilitiesTo.size(), maxDistanceInMeter);

		SpatialIndex spatialIndex = new STRtree();
		GeometryFactory gf = new GeometryFactory();
		
		for (HasGeoCoordinates to : facilitiesTo) {
			Coordinate coordinate = getCoordinateFromGeoCoordinate(to.getGeoLat(), to.getGeoLon());
			org.locationtech.jts.geom.Point point = gf.createPoint(coordinate);
			spatialIndex.insert(point.getEnvelopeInternal(), to);
		}
		

		List<DistanceBean> distances = new ArrayList<>();

		// make sure the radius is big enough
		double radius = getRadius(maxDistanceInMeter * 3);
		for (HasGeoCoordinates from : facilitiesFrom) {
			Coordinate coordinate = getCoordinateFromGeoCoordinate(from.getGeoLat(), from.getGeoLon());
			Geometry circle = createCircle(coordinate, radius);
			@SuppressWarnings("unchecked")
			List<HasGeoCoordinates> items = spatialIndex.query(circle.getEnvelopeInternal());

			Point ptFrom = getPointFromGeoCoordinate(from);
			List<HasGeoCoordinates> filteredItems = items.stream()
					.filter(item -> calculateDistanceBetweenPoints(ptFrom,
							getPointFromGeoCoordinate(item)) <= maxDistanceInMeter)
					.collect(Collectors.toList());
			
			DistanceBean distanceBean = new DistanceBean(from.getGeoLat(), from.getGeoLon(), filteredItems.size());
			distances.add(distanceBean);
		}
		

		logger.debug("created {} distances", distances.size());
		return distances;
	}

	private double getRadius(int distanceInMeter) {
		return DistanceUtils.dist2Degrees(distanceInMeter, DistanceUtils.EARTH_MEAN_RADIUS_KM * 1000);
	}

	private Coordinate getCoordinateFromGeoCoordinate(BigDecimal geoLat, BigDecimal geoLon) {
		return new Coordinate(geoLon.doubleValue(), geoLat.doubleValue());
	}

	/**
	 * https://stackoverflow.com/a/32322289
	 * 
	 * @param x
	 * @param y
	 * @param RADIUS
	 * @return
	 */
	private static Geometry createCircle(Coordinate coordinate, final double RADIUS) {
		GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
		shapeFactory.setNumPoints(64);
		shapeFactory.setCentre(coordinate);
		shapeFactory.setSize(RADIUS * 2);
		return shapeFactory.createCircle();
	}

	private double calculateDistanceBetweenPoints(Point fromPoint, Point toPoint) {
		double distanceInDeg = spatialContext.calcDistance(fromPoint, toPoint);
		double distance = DistanceUtils.degrees2Dist(distanceInDeg, DistanceUtils.EARTH_MEAN_RADIUS_KM * 1000);
		return distance;
	}

	private Point getPointFromGeoCoordinate(HasGeoCoordinates item) {
		return new PointImpl(item.getGeoLon().doubleValue(), item.getGeoLat().doubleValue(), spatialContext);
	}

}
