package at.c02.aai.app.service.bean;

import java.math.BigDecimal;

public interface HasGeoCoordinates {
    BigDecimal getGeoLat();

    BigDecimal getGeoLon();
}
