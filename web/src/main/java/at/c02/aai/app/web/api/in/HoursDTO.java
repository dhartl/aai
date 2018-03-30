package at.c02.aai.app.web.api.in;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HoursDTO {
    /**
     * Day of the week; MON, TUE, WED, THU, FRI, SAT, SUN
     */
    private String weekday;
    /**
     * Begin of Shift; 08:00
     */
	@JsonProperty("from_time")
    private String from;
    /**
     * End of Shift; 12:00
     */
	@JsonProperty("to_time")
    private String to;

	public String getWeekday() {
		return weekday;
	}

	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

}
