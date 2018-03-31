package at.c02.aai.app.web.ui;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addon.leaflet.LLayerGroup;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leafletheat.LHeatMapLayer;
import org.vaadin.addon.leafletheat.Point3D;

import com.vaadin.annotations.Theme;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;

import at.c02.aai.app.db.entity.Facility;
import at.c02.aai.app.db.entity.FacilityType;
import at.c02.aai.app.db.repository.FacilityRepository;
import at.c02.aai.app.service.DistanceService;
import at.c02.aai.app.service.bean.DistanceBean;

@SpringUI(path = "/ui")
@Theme("aai")
public class VaadinUI extends UI {

	private static final Logger logger = LoggerFactory.getLogger(VaadinUI.class);

	private static final long serialVersionUID = 1L;

	@Autowired
	private FacilityRepository facilityRepository;

	@Autowired
	private DistanceService distanceService;

	@Override
	protected void init(VaadinRequest request) {
		LMap map = new LMap();
		LTileLayer basemapLayer = new LBasemapLayer();
		map.addBaseLayer(basemapLayer, "Basemap");
		map.setView(47.069241d, 15.438473d, 13d);
		// createDoctorMarkers(map);
		// createHeatmap(map, getDoctorPoints());
		// createPharmacyMarkers(map);

		createHeatmap(map, getPharmaciesDoctorPoints());
		setContent(map);

	}

	private void createDoctorMarkers(LMap map) {
		LLayerGroup doctorMarkerGroup = new LLayerGroup();
		List<Facility> doctors = facilityRepository.findByFacilityType(FacilityType.DOCTOR);
		doctors.stream().map(this::createMarker).forEach(doctorMarkerGroup::addComponent);
		map.addLayer(doctorMarkerGroup);
	}

	private void createPharmacyMarkers(LMap map) {
		LLayerGroup doctorMarkerGroup = new LLayerGroup();
		List<Facility> doctors = facilityRepository.findByFacilityType(FacilityType.PHARMACY);
		doctors.stream().map(this::createMarker).forEach(doctorMarkerGroup::addComponent);
		map.addLayer(doctorMarkerGroup);
	}

	private void createHeatmap(LMap map, Point[] points) {

		LHeatMapLayer heatmapLayer = new LHeatMapLayer();
		heatmapLayer.setPoints(points);
		HashMap<Double, String> gradient = new LinkedHashMap<>();
		gradient.put(0.4, "blue");
		gradient.put(0.65, "lime");
		gradient.put(1.0, "red");

		heatmapLayer.setGradient(gradient);

		heatmapLayer.setRadius(20);
		heatmapLayer.setBlur(30);
		heatmapLayer.setMaxZoom(14);

		map.addLayer(heatmapLayer);
	}

	private Point[] getDoctorPoints() {
		logger.debug("before requesting distances for heatmap");
		List<Facility> doctors = facilityRepository.findByFacilityType(FacilityType.DOCTOR);
		List<DistanceBean> distances = distanceService.createDistances(doctors, doctors, 500);
		logger.debug("found {} distances for heatmap", distances.size());
		List<Point3D> points = distances.stream().map(distance -> new Point3D(distance.getGeoLat().doubleValue(),
				distance.getGeoLon().doubleValue(), distance.getCount() / 10d)).collect(Collectors.toList());
		return points.toArray(new Point[0]);
	}

	private Point[] getPharmaciesDoctorPoints() {
		logger.debug("before requesting distances for heatmap");
		List<Facility> pharmacies = facilityRepository.findByFacilityType(FacilityType.PHARMACY);
		List<Facility> doctors = facilityRepository.findByFacilityType(FacilityType.DOCTOR);

		List<DistanceBean> distances = distanceService.createDistances(pharmacies, doctors, 1000);
		logger.debug("found {} distances for heatmap", distances.size());
		List<Point3D> points = distances.stream().map(distance -> new Point3D(distance.getGeoLat().doubleValue(),
				distance.getGeoLon().doubleValue(), distance.getCount() / 5d)).collect(Collectors.toList());
		return points.toArray(new Point[0]);
	}

	private LMarker createMarker(Facility facility) {
		if (facility.getGeoLat() == null || facility.getGeoLon() == null) {
			return null;
		}
		LMarker marker = new LMarker(new Point(facility.getGeoLat().doubleValue(), facility.getGeoLon().doubleValue()));
		marker.setIcon(getFacilityImage(facility.getFacilityType()));
		marker.setPopup(facility.getTitle());
		marker.setIconAnchor(new Point(16, 37));
		return marker;
	}

	private Resource getFacilityImage(FacilityType facilityType) {
		switch (facilityType) {
		case DOCTOR:
			return new ThemeResource("images/doctor.png");
		case PHARMACY:
			return new ThemeResource("images/pharmacy.png");
		default:
			break;
		}
		return null;
	}
}