package at.c02.aai.app.db.entity;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hours")
public class Hours extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "hoursId")
	private Long hoursId;
	@Enumerated(EnumType.STRING)
	@Column(name = "weekday", nullable = false)
	private WeekDay weekday;
	@Column(name = "fromTime", nullable = false)
	private LocalTime fromTime;
	@Column(name = "toTime", nullable = false)
	private LocalTime toTime;

	@Override
	public Long getId() {
		return getHoursId();
	}

	public Long getHoursId() {
		return hoursId;
	}

	public void setHoursId(Long hoursId) {
		this.hoursId = hoursId;
	}

	public WeekDay getWeekday() {
		return weekday;
	}

	public void setWeekday(WeekDay weekday) {
		this.weekday = weekday;
	}

	public LocalTime getFromTime() {
		return fromTime;
	}

	public void setFromTime(LocalTime fromTime) {
		this.fromTime = fromTime;
	}

	public LocalTime getToTime() {
		return toTime;
	}

	public void setToTime(LocalTime toTime) {
		this.toTime = toTime;
	}

}
