package io.praegus.bda.locationservice.adapter.rest;

import io.praegus.bda.locationservice.business.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.openapitools.api.LocationApi;
import org.openapitools.model.Address;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@Log4j2
@RequiredArgsConstructor
public class LocationController implements LocationApi {

    private final LocationService locationService;

    @Override
    public ResponseEntity<Address> retrieveLocation(Address address) {
        var retrievedAddress = locationService.retrieveLocation(address);
        return retrievedAddress.getValid() ? ResponseEntity.ok(retrievedAddress) : ResponseEntity.notFound().build();
    }
}
