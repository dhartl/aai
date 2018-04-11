package at.c02.aai.app.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "specialitySynonyme")
public class SpecialitySynonyme extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specialitySynonymeId")
    private Long specialitySynonymeId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "specialityId")
    private Speciality speciality;

    @Override
    public Long getId() {
	return getSpecialitySynonymeId();
    }

    public Long getSpecialitySynonymeId() {
	return specialitySynonymeId;
    }

    public void setSpecialitySynonymeId(Long specialitySynonymeId) {
	this.specialitySynonymeId = specialitySynonymeId;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Speciality getSpeciality() {
	return speciality;
    }

    public void setSpeciality(Speciality speciality) {
	this.speciality = speciality;
    }

}
