package at.c02.aai.app.service.bean;

import java.math.BigDecimal;

public class GeoCoordinate implements HasGeoCoordinates {

	private BigDecimal geoLat;
	private BigDecimal geoLon;

	public GeoCoordinate(BigDecimal geoLat, BigDecimal geoLon) {
		super();
		this.geoLat = geoLat;
		this.geoLon = geoLon;
	}

	@Override
	public BigDecimal getGeoLat() {
		return geoLat;
	}

	public void setGeoLat(BigDecimal geoLat) {
		this.geoLat = geoLat;
	}

	@Override
	public BigDecimal getGeoLon() {
		return geoLon;
	}

	public void setGeoLon(BigDecimal geoLon) {
		this.geoLon = geoLon;
	}

	@Override
	public String toString() {
		return "GeoCoordinate [geoLat=" + geoLat + ", geoLon=" + geoLon + "]";
	}

}
