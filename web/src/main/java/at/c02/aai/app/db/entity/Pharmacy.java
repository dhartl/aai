package at.c02.aai.app.db.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pharmacy")
public class Pharmacy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pharmacyId")
    private Long pharmacyId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "facilityId", nullable = false)
    private Facility facility;

    public Long getPharmacyId() {
	return pharmacyId;
    }

    public void setPharmacyId(Long pharmacyId) {
	this.pharmacyId = pharmacyId;
    }

    public Facility getFacility() {
	return facility;
    }

    public void setFacility(Facility facility) {
	this.facility = facility;
    }

}
