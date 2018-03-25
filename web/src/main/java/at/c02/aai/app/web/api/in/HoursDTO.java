package at.c02.aai.app.web.api.in;

public class HoursDTO {
    /**
     * Day of the week; MON, TUE, WED, THU, FRI, SAT, SUN
     */
    private String weekday;
    /**
     * Begin of Shift; 08:00
     */
    private String from;
    /**
     * End of Shift; 12:00
     */
    private String to;
}
