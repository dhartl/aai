package at.c02.aai.app.db.entity;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "speciality")
public class Speciality extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specialityId")
    private Long specialityId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "specialityId")
    private Set<SpecialitySynonyme> synonymes = new LinkedHashSet<>();

    @Override
    public Long getId() {
	return getSpecialityId();
    }

    public Long getSpecialityId() {
	return specialityId;
    }

    public void setSpecialityId(Long specialityId) {
	this.specialityId = specialityId;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Set<SpecialitySynonyme> getSynonymes() {
	return synonymes;
    }

    public void setSynonymes(Set<SpecialitySynonyme> synonymes) {
	this.synonymes = synonymes;
    }

    public void addSynonyme(SpecialitySynonyme synonyme) {
	synonymes.add(synonyme);
	synonyme.setSpeciality(this);
    }

}
