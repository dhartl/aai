package at.c02.aai.app.db.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "facility")
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facilityId")
    private Long facilityId;
    @Enumerated(EnumType.STRING)
    @Column(name = "facilityType")
    private FacilityType facilityType;
    @Column(name = "title", length = 255)
    private String title;
    @Column(name = "street", length = 255)
    private String street;
    @Column(name = "houseNumber", length = 100)
    private String houseNumber;
    @Column(name = "zipCode", length = 20)
    private String zipCode;
    @Column(name = "city", length = 255)
    private String city;
    /**
     * Geo-Latitude WGS84
     */
    @Column(name = "geoLat")
    private BigDecimal geoLat;
    /**
     * Geo-Longitude WGS84
     */
    @Column(name = "geoLon")
    private BigDecimal geoLon;

    public Long getFacilityId() {
	return facilityId;
    }

    public void setFacilityId(Long facilityId) {
	this.facilityId = facilityId;
    }

    public FacilityType getFacilityType() {
	return facilityType;
    }

    public void setFacilityType(FacilityType facilityType) {
	this.facilityType = facilityType;
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
