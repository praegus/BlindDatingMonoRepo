package io.praegus.bda.locationservice.adapter.rest;

import io.praegus.bda.locationservice.business.LocationService;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.DateLocationApi;
import org.openapitools.model.Address;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DateLocationController implements DateLocationApi {

    private final LocationService locationService;

    @Override
    public ResponseEntity<Address> generateDateLocation(List<Address> addresses) {
        return locationService.generateDateLocation(addresses).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
