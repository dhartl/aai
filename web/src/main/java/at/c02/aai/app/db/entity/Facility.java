package at.c02.aai.app.db.entity;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "facility")
public class Facility extends BaseEntity {

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
    @Column(name = "zipCode", length = 20)
    private String zipCode;
    @Column(name = "city", length = 255)
    private String city;
	@Column(name = "state", length = 255)
	private String state;
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

	@Column(name = "telephoneNr", length = 100)
	private String telephoneNr;
	@Column(name = "email", length = 255)
	private String email;
	@Column(name = "url", length = 255)
	private String url;
	@Column(name = "srcUrl", length = 255)
	private String srcUrl;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "facilityId")
	private Set<Hours> hours = new LinkedHashSet<>(0);

	@Override
	public Long getId() {
		return getFacilityId();
	}

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

	public String getTelephoneNr() {
		return telephoneNr;
	}

	public void setTelephoneNr(String telephoneNr) {
		this.telephoneNr = telephoneNr;
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

	public String getSrcUrl() {
		return srcUrl;
	}

	public void setSrcUrl(String srcUrl) {
		this.srcUrl = srcUrl;
	}

	public Set<Hours> getHours() {
		return hours;
	}

	public void setHours(Set<Hours> hours) {
		this.hours = hours;
	}



}
