package at.c02.aai.app.service.bean;

import java.math.BigDecimal;

public class DistanceBean implements HasGeoCoordinates {
	private BigDecimal geoLat;
	private BigDecimal geoLon;
	private long count;

	public DistanceBean(BigDecimal geoLat, BigDecimal geoLon, long count) {
		super();
		this.geoLat = geoLat;
		this.geoLon = geoLon;
		this.count = count;
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

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "DistanceBean [geoLat=" + geoLat + ", geoLon=" + geoLon + ", count=" + count + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (count ^ (count >>> 32));
		result = prime * result + ((geoLat == null) ? 0 : geoLat.hashCode());
		result = prime * result + ((geoLon == null) ? 0 : geoLon.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DistanceBean other = (DistanceBean) obj;
		if (count != other.count)
			return false;
		if (geoLat == null) {
			if (other.geoLat != null)
				return false;
		} else if (!geoLat.equals(other.geoLat))
			return false;
		if (geoLon == null) {
			if (other.geoLon != null)
				return false;
		} else if (!geoLon.equals(other.geoLon))
			return false;
		return true;
	}

}
