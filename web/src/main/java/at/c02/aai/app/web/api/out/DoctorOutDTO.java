package at.c02.aai.app.web.api.out;

import java.math.BigDecimal;

import at.c02.aai.app.service.bean.HasGeoCoordinates;

public class DoctorOutDTO implements HasGeoCoordinates {
	private Long id;
	private String name;
	private BigDecimal geoLat;
	private BigDecimal geoLon;

	public DoctorOutDTO() {
		super();
	}

	public DoctorOutDTO(Long id, String name, BigDecimal geoLat, BigDecimal geoLon) {
		super();
		this.id = id;
		this.name = name;
		this.geoLat = geoLat;
		this.geoLon = geoLon;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		return "DoctorOutDTO [id=" + id + ", name=" + name + ", geoLat=" + geoLat + ", geoLon=" + geoLon + "]";
	}

}
