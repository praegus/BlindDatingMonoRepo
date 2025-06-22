package io.praegus.bda.locationservice.business;

import io.praegus.bda.locationservice.adapter.kafka.KafkaProducer;
import io.praegus.bda.locationservice.adapter.location.LocationServiceAdapter;
import io.praegus.bda.locationservice.adapter.profile.ProfileServiceAdapter;
import lombok.RequiredArgsConstructor;
import org.openapitools.client.model.Address;
import org.openapitools.client.model.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DateGenerationService {

    Logger logger = LoggerFactory.getLogger(ProfileServiceAdapter.class);

    private final ProfileServiceAdapter profileServiceAdapter;
    private final LocationServiceAdapter locationServiceAdapter;
    private final KafkaProducer kafkaProducer;

    public void generateDate(Match match) {
        var profileA = profileServiceAdapter.findProfile(match.personA());
        var profileB = profileServiceAdapter.findProfile(match.personB());

        var favoriteColorA = getFavoriteColor(profileA);
        var favoriteColorB = getFavoriteColor(profileB);

        var objectColor = favoriteColorA != null ? favoriteColorA : favoriteColorB != null ? favoriteColorB : "Geel";

        var dateAddress = getDateAddress(profileA, profileB);
        var date = new Date(match.personA(), match.personB(), dateAddress, ZonedDateTime.now().plusDays(5), "Sok (" + objectColor + ")");
        kafkaProducer.produceDate(date);
    }

    private String getFavoriteColor(Profile profile) {
        return profile.getPreferences() == null ? null
                : profile.getPreferences().getFavoriteColor();
    }

    private Address getDateAddress(Profile profileA, Profile profileB) {
        if (invalidAddress(profileA.getAddress()) || invalidAddress(profileB.getAddress())) {
            logger.info("no valid address filled, generating date with fallback address");
            return defaultAddress();
        }
        try {
            return locationServiceAdapter.generateDateLocation(List.of(profileA.getAddress(), profileB.getAddress()));
        } catch (Exception e) {
            logger.error(e.toString());
            return defaultAddress();
        }

    }

    private static Address defaultAddress() {
        var address = new Address();
        address.setStreet("De Wellenkamp");
        address.setStreetNumber("1141");
        address.setPostalCode("6545NB");
        address.setCity("Nijmegen");
        return address;
    }

    private boolean invalidAddress(org.openapitools.client.model.Address address) {
        return address == null || !Boolean.TRUE.equals(address.getValid());
    }
}
