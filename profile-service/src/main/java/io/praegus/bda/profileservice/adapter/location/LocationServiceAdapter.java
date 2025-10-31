package io.praegus.bda.profileservice.adapter.location;

import org.openapitools.client.ApiClient;
import org.openapitools.client.api.LocationsApi;
import org.openapitools.client.model.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;

@Repository
public class LocationServiceAdapter {

    Logger logger = LoggerFactory.getLogger(LocationServiceAdapter.class);

    private final LocationsApi locationsApi;

    public LocationServiceAdapter(@Value("${endpoints.locationservice}") String locationServiceEndpoint) {
        ApiClient defaultClient = new ApiClient();
        defaultClient.setBasePath(locationServiceEndpoint);

        locationsApi = new LocationsApi(defaultClient);
    }

    public Address retrieveLocation(Address address) {
        try {
            return locationsApi.retrieveLocation(address);
        } catch (HttpClientErrorException.NotFound exception) {
            logger.error("Error when retrieving location", exception);
            address.valid(false);
            return address;
        }
    }
}
