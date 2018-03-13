package at.c02.aai.app.db.entity;

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
@Table(name = "facilityDistance")
public class FacilityDistance {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "facilityDistanceId")
	private Long facilityDistanceId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "facilityStartId", nullable = false)
	private Facility start;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "facilityEndId", nullable = false)
	private Facility end;
	@Column(name = "distance", nullable = false)
	private int distance = 0;

	public Long getFacilityDistanceId() {
		return facilityDistanceId;
	}

	public void setFacilityDistanceId(Long facilityDistanceId) {
		this.facilityDistanceId = facilityDistanceId;
	}

	public Facility getStart() {
		return start;
	}

	public void setStart(Facility start) {
		this.start = start;
	}

	public Facility getEnd() {
		return end;
	}

	public void setEnd(Facility end) {
		this.end = end;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

}
