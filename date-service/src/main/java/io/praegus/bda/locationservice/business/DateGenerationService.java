package io.praegus.bda.locationservice.business;

import com.example.Match;
import com.example.matching.Address;
import com.example.matching.ScheduledDate;
import io.praegus.bda.locationservice.adapter.kafka.KafkaProducer;
import io.praegus.bda.locationservice.adapter.location.LocationServiceAdapter;
import io.praegus.bda.locationservice.adapter.profile.ProfileServiceAdapter;
import lombok.RequiredArgsConstructor;
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
        var profileA = profileServiceAdapter.findProfile(match.getPersonA().toString());
        var profileB = profileServiceAdapter.findProfile(match.getPersonB().toString());

        var favoriteColorA = getFavoriteColor(profileA);
        var favoriteColorB = getFavoriteColor(profileB);

        var objectColor = favoriteColorA != null ? favoriteColorA : favoriteColorB != null ? favoriteColorB : "Geel";

        var dateAddress = getDateAddress(profileA, profileB);
        var date = new ScheduledDate(match.getPersonA().toString(), match.getPersonB().toString(), dateAddress, ZonedDateTime.now().plusDays(5).toInstant(), "Sok (" + objectColor + ")");
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
            var locationAddress = locationServiceAdapter.generateDateLocation(List.of(profileA.getAddress(), profileB.getAddress()));
            var address = new Address();
            address.setCity(locationAddress.getCity());
            address.setStreet(locationAddress.getStreet());
            address.setStreetNumber(locationAddress.getStreetNumber());
            address.setPostalCode(locationAddress.getPostalCode());
            address.setValid(locationAddress.getValid());
            address.setLatitude(locationAddress.getLatitude() != null ? locationAddress.getLatitude().doubleValue() : null);
            address.setLongitude(locationAddress.getLongitude() != null ? locationAddress.getLongitude().doubleValue() : null);
            return address;
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
