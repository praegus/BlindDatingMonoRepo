package io.praegus.bda.locationservice.adapter.location;

import org.openapitools.client.ApiClient;
import org.openapitools.client.api.DateLocationsApi;
import org.openapitools.client.model.Address;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LocationServiceAdapter {
    private final DateLocationsApi dateLocationsApi;

    public LocationServiceAdapter(@Value("${endpoints.locationservice}") String locationServiceEndpoint) {
        ApiClient defaultClient = new ApiClient();
        defaultClient.setBasePath(locationServiceEndpoint);

        dateLocationsApi = new DateLocationsApi(defaultClient);
    }

    public Address generateDateLocation(List<Address> addresses) {
        return dateLocationsApi.generateDateLocation(addresses);
    }
}
