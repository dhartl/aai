package at.c02.aai.app.web.api.out;

import java.math.BigDecimal;

public class HeatMapItemDTO {
	private BigDecimal geoLat;
	private BigDecimal geoLon;
	private BigDecimal intensity;

	public HeatMapItemDTO() {
		super();
	}

	public HeatMapItemDTO(BigDecimal geoLat, BigDecimal geoLon, BigDecimal intensity) {
		super();
		this.geoLat = geoLat;
		this.geoLon = geoLon;
		this.intensity = intensity;
	}

	public BigDecimal getGeoLat() {
		return geoLat;
	}

	public void setGeoLat(BigDecimal geoLat) {
		this.geoLat = geoLat;
	}

	public BigDecimal getGeoLon() {
		return geoLon;
	}

	public void setGeoLon(BigDecimal geoLon) {
		this.geoLon = geoLon;
	}

	public BigDecimal getIntensity() {
		return intensity;
	}

	public void setIntensity(BigDecimal intensity) {
		this.intensity = intensity;
	}

	@Override
	public String toString() {
		return "HeatMapItemDTO [geoLat=" + geoLat + ", geoLon=" + geoLon + ", intensity=" + intensity + "]";
	}

}
