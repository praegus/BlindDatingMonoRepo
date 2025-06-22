package io.praegus.bda.locationservice.business;

import io.praegus.bda.locationservice.adapter.apipostcode.ApiPostCodeClient;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.Address;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final ApiPostCodeClient apiPostCodeClient;

    public Address retrieveLocation(Address address) {
        return apiPostCodeClient.retrieveLocation(address);
    }
}
