package io.praegus.bda.matchingservice.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.praegus.bda.matchingservice.adapter.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MatchingReceiver {

    Logger logger = LoggerFactory.getLogger(MatchingReceiver.class);
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    private final KafkaProducer kafkaProducer;

    private final Map<String, String> matchStatuses = new HashMap<>();

    @MessageMapping("/accept/{username}")
    public void receiveAcknowledgement(@DestinationVariable String username, String message) {
        logger.info("Received acknowledgement from {}: {}", username, message);
        try {
            var match = objectMapper.readValue(message, Match.class);
            if (!matchStatuses.containsKey(match.toKey())) {
                throw new IllegalArgumentException("key " + match.toKey() + " not known");
            }
            if (matchStatuses.get(match.toKey()).equalsIgnoreCase("1:1")) {
                logger.info("date is already being set up, ignoring new flow");
                return;
            }

            var currentStatuses = matchStatuses.get(match.toKey()).split(":");

            if (username.equalsIgnoreCase(match.personA())) {
                currentStatuses[0] = "1";
            } else {
                currentStatuses[1] = "1";
            }
            matchStatuses.put(match.toKey(), currentStatuses[0] + ":" + currentStatuses[1]);

            if ("1".equalsIgnoreCase(currentStatuses[0]) && "1".equalsIgnoreCase(currentStatuses[1])) {
                logger.info("both agreed to a date!");
                messagingTemplate.convertAndSend("/topic/approved/" + match.personA(), message);
                messagingTemplate.convertAndSend("/topic/approved/" + match.personB(), message);
                kafkaProducer.produceDateApproval(message);
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "matchings", groupId = "ws")
    public void listenForMatches(String message) {
        logger.info("Received Message in group foo: " + message);
        try {
            var match = objectMapper.readValue(message, Match.class);
            if (matchStatuses.containsKey(match.toKey()) && matchStatuses.get(match.toKey()).equalsIgnoreCase("1:1")) {
                logger.info("date is already being setup, skip new notifications");
                return;
            }
            matchStatuses.put(match.toKey(), "0:0");
            messagingTemplate.convertAndSend("/topic/matchings/" + match.personA(), message);
            messagingTemplate.convertAndSend("/topic/matchings/" + match.personB(), message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "dates", groupId = "ws")
    public void listenForDates(String message) {
        logger.info("Received date in group foo: " + message);
    }
}
