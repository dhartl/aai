package at.c02.aai.app.web.ui;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LTileLayer;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;

@SpringUI(path = "/ui")
public class VaadinUI extends UI {

    private static final long serialVersionUID = 1L;

    @Override
    protected void init(VaadinRequest request) {
	LMap map = new LMap();
	LTileLayer basemapLayer = new LBasemapLayer();
	map.addBaseLayer(basemapLayer, "Basemap");
	map.setView(47.069241d, 15.438473d, 13d);
	setContent(map);

    }
}