package io.praegus.bda.locationservice.adapter.overpass;

import org.openapitools.model.Address;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Repository
public class OverPassClient {

    RestTemplate restTemplate = new RestTemplate();

    public Optional<Address> getRestaurants(BigDecimal longitude, BigDecimal lattitude) {
        String query = "[out:json];node[\"amenity\"=\"restaurant\"](around:5000,"+lattitude+","+longitude+");out;";
        String overpassUrl = "https://overpass-api.de/api/interpreter";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>("data=" + query, headers);

        ResponseEntity<OverPassResponse> response = restTemplate.postForEntity(overpassUrl, request, OverPassResponse.class);
        var addresses = Objects.requireNonNull(response.getBody()).elements.stream().map(e -> Address.builder()
                        .city(e.tags.get("addr:city"))
                        .street(e.tags.get("addr:street"))
                        .streetNumber(e.tags.get("addr:housenumber"))
                        .postalCode(e.tags.get("addr:postcode"))
                        .build())
                .filter(a -> a.getCity() != null)
                .filter(a -> a.getStreet() != null)
                .filter(a -> a.getStreetNumber() != null)
                .filter(a -> a.getPostalCode() != null)
                .toList();
        if (addresses.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(addresses.get(0));
    }
}
