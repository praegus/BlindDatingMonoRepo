package io.praegus.bda.matchingservice.adapter.profile;

import org.openapitools.client.ApiClient;
import org.openapitools.client.api.ProfilesApi;
import org.openapitools.client.model.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProfileServiceAdapter {

    Logger logger = LoggerFactory.getLogger(ProfileServiceAdapter.class);

    private final ProfilesApi profilesApi;

    public ProfileServiceAdapter() {
        ApiClient defaultClient = new ApiClient();
        defaultClient.setBasePath("http://localhost:9080");

        profilesApi = new ProfilesApi(defaultClient);
    }

    public Profile findProfile(String username) {
        return profilesApi.getSingleProfile(username);
    }
}
