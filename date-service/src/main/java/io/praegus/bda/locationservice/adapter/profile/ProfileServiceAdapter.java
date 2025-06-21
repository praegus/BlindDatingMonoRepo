package io.praegus.bda.locationservice.adapter.profile;

import org.openapitools.client.ApiClient;
import org.openapitools.client.api.ProfilesApi;
import org.openapitools.client.model.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class ProfileServiceAdapter {

    Logger logger = LoggerFactory.getLogger(ProfileServiceAdapter.class);

    private final ProfilesApi profilesApi;

    public ProfileServiceAdapter(@Value("${endpoints.profileservice}") String profileServiceEndpoint) {
        ApiClient defaultClient = new ApiClient();
        defaultClient.setBasePath(profileServiceEndpoint);

        profilesApi = new ProfilesApi(defaultClient);
    }

    public Profile findProfile(String username) {
        return profilesApi.getSingleProfile(username);
    }
}
