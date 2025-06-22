package io.praegus.bda.locationservice.adapter.apipostcode;

import org.openapitools.model.Address;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Collections;

@Repository
public class ApiPostCodeClient {

    RestTemplate restTemplate = new RestTemplate();

    public Address retrieveLocation(Address address) {
        var response = getAddressResponse(address.getPostalCode(), address.getStreetNumber());

        if (response.getStatusCode().value() != 200) {
            address.setValid(false);
            return address;
        }

        address.setLongitude(new BigDecimal(String.valueOf(response.getBody().longitude())));
        address.setLatitude(new BigDecimal(String.valueOf(response.getBody().latitude())));
        address.setValid(true);
        return address;
    }

    private ResponseEntity<AddressResponse> getAddressResponse(String zipCode, String houseNumber) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("zipcode", zipCode);
        map.add("housenumber", houseNumber);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        try {
            return restTemplate.postForEntity("https://api-postcode.nl/api/postcode", request, AddressResponse.class);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
