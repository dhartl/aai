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
@Table(name = "doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctorId")
    private Long doctorId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "facilityId", nullable = false)
    private Facility facility;

    public Long getDoctorId() {
	return doctorId;
    }

    public void setDoctorId(Long doctorId) {
	this.doctorId = doctorId;
    }

    public Facility getFacility() {
	return facility;
    }

    public void setFacility(Facility facility) {
	this.facility = facility;
    }

}
