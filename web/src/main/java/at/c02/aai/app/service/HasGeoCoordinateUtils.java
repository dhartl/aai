package at.c02.aai.app.service;

import java.math.BigDecimal;

import at.c02.aai.app.service.bean.HasGeoCoordinates;

public class HasGeoCoordinateUtils {

    public static boolean hasCoordinates(HasGeoCoordinates facility) {
	return isCoordinate(facility.getGeoLat()) && isCoordinate(facility.getGeoLon());
    }

    public static boolean isCoordinate(BigDecimal value) {
	return value != null && value.compareTo(BigDecimal.ZERO) > 0 && value.compareTo(BigDecimal.valueOf(180)) < 0;
    }

}
