package io.praegus.bda.locationservice.business;

import io.praegus.bda.locationservice.adapter.kafka.KafkaProducer;
import io.praegus.bda.locationservice.adapter.profile.ProfileServiceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class DateGenerationService {

    private final ProfileServiceAdapter profileServiceAdapter;
    private final KafkaProducer kafkaProducer;

    public void generateDate(Match match) {
        var profileA = profileServiceAdapter.findProfile(match.personA());
        var profileB = profileServiceAdapter.findProfile(match.personB());

        var dateAddress = new Address("De Wellenkamp", "1141","6545NB", "Nijmegen");
        var date = new Date(match.personA(), match.personB(), dateAddress, ZonedDateTime.now(), "Gele Sok");
        kafkaProducer.produceDate(date);
    }
}
