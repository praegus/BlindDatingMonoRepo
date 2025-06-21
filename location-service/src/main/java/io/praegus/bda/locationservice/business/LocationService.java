package io.praegus.bda.locationservice.business;

import io.praegus.bda.locationservice.adapter.apipostcode.ApiPostCodeClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final ApiPostCodeClient apiPostCodeClient;

    public boolean isAddressValid(String postalCode, String houseNumber) {
        return apiPostCodeClient.isAddressValid(postalCode, houseNumber);
    }
}
