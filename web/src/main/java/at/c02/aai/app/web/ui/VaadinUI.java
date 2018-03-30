package at.c02.aai.app.web.ui;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addon.leaflet.LLayerGroup;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.annotations.Theme;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;

import at.c02.aai.app.db.entity.Doctor;
import at.c02.aai.app.db.repository.DoctorRepository;

@SpringUI(path = "/ui")
@Theme("aai")
public class VaadinUI extends UI {

	@Autowired
	private DoctorRepository doctorRepository;

	private static final long serialVersionUID = 1L;

	@Override
	protected void init(VaadinRequest request) {
		LMap map = new LMap();
		LTileLayer basemapLayer = new LBasemapLayer();
		map.addBaseLayer(basemapLayer, "Basemap");
		map.setView(47.069241d, 15.438473d, 13d);
		LLayerGroup doctorMarkerGroup = new LLayerGroup();
		List<Doctor> doctors = doctorRepository.findAllWithFacility();
		doctors.stream().map(this::createMarker).forEach(doctorMarkerGroup::addComponent);
		map.addLayer(doctorMarkerGroup);
		setContent(map);

	}

	private LMarker createMarker(Doctor doctor) {
		if(doctor.getFacility().getGeoLat() == null || doctor.getFacility().getGeoLon() == null) {
			return null;
		}
		LMarker marker = new LMarker(new Point(doctor.getFacility().getGeoLat().doubleValue(),
				doctor.getFacility().getGeoLon().doubleValue()));
		marker.setIcon(new ThemeResource("images/doctor.png"));
		marker.setPopup(doctor.getFacility().getTitle());
		marker.setIconAnchor(new Point(16, 37));
		return marker;
	}
}