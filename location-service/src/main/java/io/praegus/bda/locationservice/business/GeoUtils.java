package io.praegus.bda.locationservice.business;

import org.openapitools.model.Address;

import java.math.BigDecimal;
import java.util.List;

public class GeoUtils {

    public static Address calculateCenter(List<Address> addresses) {
        // Convert degrees to radians
        double lat1Rad = Math.toRadians(addresses.get(0).getLatitude().doubleValue());
        double lon1Rad = Math.toRadians(addresses.get(0).getLongitude().doubleValue());
        double lat2Rad = Math.toRadians(addresses.get(1).getLatitude().doubleValue());
        double lon2Rad = Math.toRadians(addresses.get(1).getLongitude().doubleValue());

        // Convert to Cartesian coordinates
        double x1 = Math.cos(lat1Rad) * Math.cos(lon1Rad);
        double y1 = Math.cos(lat1Rad) * Math.sin(lon1Rad);
        double z1 = Math.sin(lat1Rad);

        double x2 = Math.cos(lat2Rad) * Math.cos(lon2Rad);
        double y2 = Math.cos(lat2Rad) * Math.sin(lon2Rad);
        double z2 = Math.sin(lat2Rad);

        // Average the Cartesian coordinates
        double x = (x1 + x2) / 2;
        double y = (y1 + y2) / 2;
        double z = (z1 + z2) / 2;

        // Convert back to latitude and longitude
        double lonMid = Math.atan2(y, x);
        double hyp = Math.sqrt(x * x + y * y);
        double latMid = Math.atan2(z, hyp);

        return Address.builder()
                .latitude(BigDecimal.valueOf(Math.toDegrees(latMid)))
                .longitude(BigDecimal.valueOf(Math.toDegrees(lonMid)))
                .build();
    }
}