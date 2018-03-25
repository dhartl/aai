package at.c02.aai.app.web.api.in;

import java.math.BigDecimal;
import java.util.List;

public class DoctorDTO {

    private Long id;
    private String title;
    /**
     * Körblergasse 126
     */
    private String street;
    /**
     * 8010
     */
    private String zipCode;
    /**
     * Graz
     */
    private String city;
    /**
     * Steiermark
     */
    private String state;
    /**
     * Latitude in WGS84-Format; 47.089307
     */
    private BigDecimal geoLat;
    /**
     * Longitude in WGS84-Format; 15.440093
     */
    private BigDecimal geoLon;
    private String telephoneNumber;
    private String email;
    /**
     * URL to website of the doctor
     */
    private String url;
    /**
     * Speciality fields of the doctor
     */
    private List<String> specialities;
    /**
     * Open hours
     */
    private List<HoursDTO> hours;

    /**
     * URL to the source of the doctor
     */
    private String srcUrl;

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

    public String getState() {
	return state;
    }

    public void setState(String state) {
	this.state = state;
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

    public String getTelephoneNumber() {
	return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
	this.telephoneNumber = telephoneNumber;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public List<String> getSpecialities() {
	return specialities;
    }

    public void setSpecialities(List<String> specialities) {
	this.specialities = specialities;
    }

    public List<HoursDTO> getHours() {
	return hours;
    }

    public void setHours(List<HoursDTO> hours) {
	this.hours = hours;
    }

    public String getSrcUrl() {
	return srcUrl;
    }

    public void setSrcUrl(String srcUrl) {
	this.srcUrl = srcUrl;
    }

}
