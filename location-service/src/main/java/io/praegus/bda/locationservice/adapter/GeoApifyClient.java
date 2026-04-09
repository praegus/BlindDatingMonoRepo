package io.praegus.bda.locationservice.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import io.praegus.bda.locationservice.business.GeoUtils;
import org.openapitools.model.Address;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Optional;

/**
 * GeoApify is recommended by Codex as replacement for my old OverPassClient which doesnt work as well anymore (mainly triggering 504 errors)
 * https://myprojects.geoapify.com/api/aCVV9eDLlq7oPFvGjcHu/keys
 */
@Repository
public class GeoApifyClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String geoapifyApiKey;

    public GeoApifyClient(@Value("${external.geoapify.api-key}") String geoapifyApiKey) {
        this.geoapifyApiKey = geoapifyApiKey;
    }

    public Optional<Address> getRestaurants(BigDecimal longitude, BigDecimal lattitude) {
        String geoapifyUrl = UriComponentsBuilder
                .fromHttpUrl("https://api.geoapify.com/v2/places")
                .queryParam("categories", "catering.restaurant")
                .queryParam("filter", "circle:" + longitude + "," + lattitude + ",5000")
                .queryParam("limit", 20)
                .queryParam("apiKey", geoapifyApiKey)
                .toUriString();

        JsonNode response = restTemplate.getForObject(geoapifyUrl, JsonNode.class);
        if (response == null || !response.has("features")) {
            return Optional.empty();
        }

        var addresses = response.get("features").findValues("properties").stream()
                .map(properties -> Address.builder()
                        .city(textOrNull(properties, "city"))
                        .street(textOrNull(properties, "street"))
                        .streetNumber(textOrNull(properties, "housenumber"))
                        .postalCode(textOrNull(properties, "postcode"))
                        .latitude(decimalOrNull(properties, "lat"))
                        .longitude(decimalOrNull(properties, "lon"))
                        .build())
                .filter(a -> a.getCity() != null)
                .filter(a -> a.getStreet() != null)
                .filter(a -> a.getStreetNumber() != null)
                .filter(a -> a.getPostalCode() != null)
                .filter(a -> a.getLongitude() != null)
                .filter(a -> a.getLatitude() != null)
                .toList();
        if (addresses.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(addresses.stream()
                .min(Comparator.comparingDouble(a -> GeoUtils.calculateDistance(a.getLatitude().doubleValue(), a.getLongitude().doubleValue(), lattitude.doubleValue(), longitude.doubleValue())))
                .orElse(null));
    }

    private String textOrNull(JsonNode node, String fieldName) {
        JsonNode value = node.get(fieldName);
        return value == null || value.isNull() ? null : value.asText();
    }

    private BigDecimal decimalOrNull(JsonNode node, String fieldName) {
        JsonNode value = node.get(fieldName);
        return value == null || value.isNull() ? null : value.decimalValue();
    }
}
