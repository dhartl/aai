package at.c02.aai.app.service;

import java.math.BigDecimal;

import at.c02.aai.app.db.entity.Facility;

public class FacilityUtils {
	public static boolean hasCoordinates(Facility facility) {
		return isCoordinate(facility.getGeoLat()) && isCoordinate(facility.getGeoLon());
	}

	public static boolean isCoordinate(BigDecimal value) {
		return value != null && value.compareTo(BigDecimal.ZERO) > 0 && value.compareTo(BigDecimal.valueOf(180)) < 0;
	}
}
