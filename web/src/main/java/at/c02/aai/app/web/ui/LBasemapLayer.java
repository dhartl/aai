package at.c02.aai.app.web.ui;

import org.vaadin.addon.leaflet.LTileLayer;

public class LBasemapLayer extends LTileLayer{

    private static final long serialVersionUID = 1756429034644496907L;

    public LBasemapLayer() {
	super("https://{s}.wien.gv.at/basemap/geolandbasemap/normal/google3857/{z}/{y}/{x}.png");
	setAttributionString("Tiles &copy; <a href=\"//www.basemap.at/\">basemap.at</a> (STANDARD)");
	setSubDomains("maps1", "maps2", "maps3", "maps4");
	setMaxZoom(18);
	setId("basemap.normal");
    }
}
