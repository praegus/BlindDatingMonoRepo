package io.praegus.bda.profileservice.business;

import io.praegus.bda.profileservice.SupportsPostgresIntegrationTests;
import io.praegus.bda.profileservice.adapter.data.ProfileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.model.Profile;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileServiceCreateProfileTest extends SupportsPostgresIntegrationTests {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    @DisplayName("profile-lifecycle.create-new-profile")
    void shouldCreateAndPersistNewProfileWithInitializedSubstructures() {
        // OpenSpec: profile-lifecycle.create-new-profile
        stubUnresolvedLocationLookup();

        var username = "user" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        var profile = new Profile();
        profile.setUsername(username);

        profileService.createProfile(profile);

        var persistedProfile = profileRepository.findById(username);

        assertThat(persistedProfile).isPresent();
        assertThat(persistedProfile.get().getUsername()).isEqualTo(username);
        assertThat(persistedProfile.get().getAddress()).isNotNull();
        assertThat(persistedProfile.get().getPersonalInformation()).isNotNull();
        assertThat(persistedProfile.get().getPreferences()).isNotNull();
        assertThat(persistedProfile.get().getDislikes()).isNotNull();
        assertThat(persistedProfile.get().getDates()).isEmpty();

        verifyLocationLookupCalled();
    }
}
