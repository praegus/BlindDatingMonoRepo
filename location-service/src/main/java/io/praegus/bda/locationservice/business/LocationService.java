package io.praegus.bda.locationservice.business;

import io.praegus.bda.locationservice.adapter.apipostcode.ApiPostCodeClient;
import io.praegus.bda.locationservice.adapter.overpass.OverPassClient;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.Address;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final ApiPostCodeClient apiPostCodeClient;
    private final OverPassClient overPassClient;

    public Address retrieveLocation(Address address) {
        return apiPostCodeClient.retrieveLocation(address);
    }

    public Optional<Address> generateDateLocation(List<Address> addresses) {
        // only works when received 2 addresses
        if (addresses.size() != 2) {
            return Optional.empty();
        }

        // only works if both addresses have latitude and longitude coordinates
        if (addresses.stream().anyMatch(a -> a.getLatitude() == null && a.getLongitude() == null)) {
            return Optional.empty();
        }

        var centerLocation = GeoUtils.calculateCenter(addresses);

        return overPassClient.getRestaurants(centerLocation.getLongitude(), centerLocation.getLatitude());
    }
}
