package io.praegus.bda.profileservice.business;

import io.praegus.bda.profileservice.adapter.data.AddressEntity;
import io.praegus.bda.profileservice.adapter.data.DislikesEntity;
import io.praegus.bda.profileservice.adapter.data.PersonalInformationEntity;
import io.praegus.bda.profileservice.adapter.data.PreferencesEntity;
import io.praegus.bda.profileservice.adapter.data.ProfileEntity;
import io.praegus.bda.profileservice.adapter.data.ProfileRepository;
import io.praegus.bda.profileservice.adapter.location.LocationServiceAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private LocationServiceAdapter locationServiceAdapter;

    @InjectMocks
    private ProfileService profileService;

    @Test
    void shouldUpdateProfileWithMockedRepository() {
        var existingProfile = existingProfile();
        var updatedProfile = updatedProfile();
        var retrievedAddress = new org.openapitools.client.model.Address();
        retrievedAddress.setStreet(updatedProfile.getAddress().getStreet());
        retrievedAddress.setStreetNumber(updatedProfile.getAddress().getStreetNumber());
        retrievedAddress.setPostalCode(updatedProfile.getAddress().getPostalCode());
        retrievedAddress.setCity(updatedProfile.getAddress().getCity());
        retrievedAddress.setValid(true);

        when(profileRepository.findById("Fin")).thenReturn(Optional.of(existingProfile));
        when(locationServiceAdapter.retrieveLocation(any())).thenReturn(retrievedAddress);

        profileService.updateProfile("Fin", updatedProfile);

        verify(profileRepository).save(existingProfile);
        assertThat(existingProfile.getFirstname()).isEqualTo("Fin");
        assertThat(existingProfile.getLastname()).isEqualTo("Kingma");
        assertThat(existingProfile.getAdditionalInfo()).isEmpty();
        assertThat(existingProfile.getAddress().getPostalCode()).isEqualTo("1234AA");
        assertThat(existingProfile.getAddress().isValid()).isTrue();
        assertThat(existingProfile.getPersonalInformation().getGender()).isEqualTo("MAN");
        assertThat(existingProfile.getPersonalInformation().getHairColor()).isEqualTo("BROWN");
        assertThat(existingProfile.getPersonalInformation().getMusicGenres()).isEqualTo("DANCE");
    }

    private static ProfileEntity existingProfile() {
        return ProfileEntity.builder()
                .username("Fin")
                .address(AddressEntity.builder()
                        .street("Old street")
                        .streetNumber("1")
                        .postalCode("1111AA")
                        .city("Old city")
                        .build())
                .personalInformation(PersonalInformationEntity.builder().build())
                .preferences(PreferencesEntity.builder().build())
                .dislikes(DislikesEntity.builder().build())
                .build();
    }

    private static Profile updatedProfile() {
        Profile profile = new Profile();

        profile.setFirstname("Fin");
        profile.setLastname("Kingma");
        profile.setAdditionalInfo("");
        profile.setUsername("Finnn");

        Address address = new Address(
                "",
                "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789",
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
