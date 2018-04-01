package at.c02.aai.app.service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.c02.aai.app.db.entity.Facility;
import at.c02.aai.app.db.entity.Hours;
import at.c02.aai.app.db.entity.WeekDay;
import at.c02.aai.app.web.api.in.HoursDTO;

public class FacilityUtils {

	private static final Logger logger = LoggerFactory.getLogger(FacilityUtils.class);

	public static boolean hasCoordinates(Facility facility) {
		return isCoordinate(facility.getGeoLat()) && isCoordinate(facility.getGeoLon());
	}

	public static boolean isCoordinate(BigDecimal value) {
		return value != null && value.compareTo(BigDecimal.ZERO) > 0 && value.compareTo(BigDecimal.valueOf(180)) < 0;
	}

	public static Hours mapFromHoursDto(HoursDTO hoursDto) {
		Hours hours = new Hours();
		hours.setWeekday(WeekDay.valueOf(hoursDto.getWeekday()));
		try {
			hours.setFromTime(parseTime(hoursDto.getFrom()));
			hours.setToTime(parseTime(hoursDto.getTo()));
		} catch (DateTimeParseException ex) {
			logger.error("failed to parse {}", hoursDto);
			return null;
		}
		return hours;
	}

	public static HoursDTO mapToHoursDto(Hours hours) {
		HoursDTO hoursDto = new HoursDTO();
		hoursDto.setWeekday(hours.getWeekday().toString());
		DateTimeFormatter hoursFormat = getHoursFormat();
		hoursDto.setFrom(hoursFormat.format(hours.getFromTime()));
		hoursDto.setTo(hoursFormat.format(hours.getToTime()));

		return hoursDto;
	}

	private static LocalTime parseTime(String time) {
		LocalTime localTime = null;
		List<DateTimeFormatter> formatters = getFormatters();
		DateTimeParseException exception = null;
		for (DateTimeFormatter formatter : formatters) {
			try {
				localTime = LocalTime.parse(time, formatter);
			} catch (DateTimeParseException ex) {
				exception = ex;
			}
		}
		if (localTime == null) {
			throw exception;
		}
		return localTime;
	}

	private static List<DateTimeFormatter> getFormatters() {
		return Arrays.asList(DateTimeFormatter.ofPattern("HH:mm"), DateTimeFormatter.ofPattern("H:mm"),
				DateTimeFormatter.ofPattern("HH:m"), DateTimeFormatter.ofPattern("H:m"));
	}

	private static DateTimeFormatter getHoursFormat() {
		return DateTimeFormatter.ofPattern("HH:mm");
	}

}
