package at.c02.aai.app.web.api;

import java.math.BigDecimal;

public class PharmacyDTO {
    private Long id;
    private String title;
    private String street;
    private String houseNumber;
    private String zipCode;
    private String city;
    private BigDecimal geoLat;
    private BigDecimal geoLon;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getStreet() {
	return street;
    }

    public void setStreet(String street) {
	this.street = street;
    }

    public String getHouseNumber() {
	return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
	this.houseNumber = houseNumber;
    }

    public String getZipCode() {
	return zipCode;
    }

    public void setZipCode(String zipCode) {
	this.zipCode = zipCode;
    }

    public String getCity() {
	return city;
    }

    public void setCity(String city) {
	this.city = city;
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

}
