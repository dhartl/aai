package at.c02.aai.app.service.bean;

import java.math.BigDecimal;

public class DistanceBean {
	private BigDecimal geoLat;
	private BigDecimal geoLon;
	private long count;

	public DistanceBean(BigDecimal geoLat, BigDecimal geoLon, long count) {
		super();
		this.geoLat = geoLat;
		this.geoLon = geoLon;
		this.count = count;
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

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

}
