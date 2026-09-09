package io.praegus.bda.profileservice.business;

import io.praegus.bda.profileservice.SupportsPostgresIntegrationTests;
import org.junit.jupiter.api.Test;
import org.openapitools.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ProfileServiceUpdateProfileTest extends SupportsPostgresIntegrationTests {

    @Autowired
    private ProfileService profileService;

    @Test
    void updateProfile_shouldNotFailWithLongStreetNumbers() {
        stubUnresolvedLocationLookup();
        var profile = defaultProfile();
        profileService.createProfile(profile);

        profile.getAddress().setStreetNumber("012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
        assertDoesNotThrow(() -> profileService.updateProfile(profile.getUsername(), profile));
    }

    private static Profile defaultProfile() {
        Profile profile = new Profile();

        profile.setFirstname("Fin");
        profile.setLastname("Kingma");
        profile.setAdditionalInfo("");
        profile.setUsername("Finnn");

        Address address = new Address(
                "",
                "123",
                "1234AA",
                "",
                false,
                null,
                null);
        profile.setAddress(address);

        Characteristics pi = new Characteristics(
                Gender.MAN,
                "",
                false,
                HairColor.BROWN,
                false,
                "",
                List.of(MusicGenre.DANCE)
        );
        profile.setPersonalInformation(pi);
        profile.setPreferences(pi);
        profile.setDislikes(pi);

        return profile;
    }
}
