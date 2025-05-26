package io.praegus.bda.profileservice.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReceiveDateService {

    Logger logger = LoggerFactory.getLogger(ReceiveDateService.class);
    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    private final ProfileService profileService;

    @KafkaListener(topics = "dates", groupId = "profile")
    public void listenForDates(String message) {
        logger.info("Received Date approval: " + message);
        try {
            var date = objectMapper.readValue(message, Date.class);
            profileService.addDateToProfile(date.personA(), date);
            profileService.addDateToProfile(date.personB(), date);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
