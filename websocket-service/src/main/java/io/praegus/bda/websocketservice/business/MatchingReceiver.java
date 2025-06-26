package io.praegus.bda.websocketservice.business;

import com.example.Match;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.praegus.bda.websocketservice.adapter.kafka.KafkaProducer;
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
        var match = fromKey(message);
        if (!matchStatuses.containsKey(toKey(match))) {
            throw new IllegalArgumentException("key " + toKey(match) + " not known");
        }
        if (matchStatuses.get(toKey(match)).equalsIgnoreCase("1:1")) {
            logger.info("date is already being set up, ignoring new flow");
            return;
        }

        var currentStatuses = matchStatuses.get(toKey(match)).split(":");

        if (username.equalsIgnoreCase(match.getPersonA().toString())) {
            currentStatuses[0] = "1";
        } else {
            currentStatuses[1] = "1";
        }
        matchStatuses.put(toKey(match), currentStatuses[0] + ":" + currentStatuses[1]);

        if ("1".equalsIgnoreCase(currentStatuses[0]) && "1".equalsIgnoreCase(currentStatuses[1])) {
            logger.info("both agreed to a date!");
            messagingTemplate.convertAndSend("/topic/approved/" + match.getPersonA(), toKey(match));
            messagingTemplate.convertAndSend("/topic/approved/" + match.getPersonB(), toKey(match));
            kafkaProducer.produceDateApproval(match);
        }
    }

    @KafkaListener(topics = "matchings", groupId = "ws")
    public void listenForMatches(Match match) {
        logger.info("Received Message in group foo: " + match);
        if (matchStatuses.containsKey(toKey(match)) && matchStatuses.get(toKey(match)).equalsIgnoreCase("1:1")) {
            logger.info("date is already being setup, skip new notifications");
            return;
        }
        matchStatuses.put(toKey(match), "0:0");
        messagingTemplate.convertAndSend("/topic/matchings/" + match.getPersonA(), toKey(match));
        messagingTemplate.convertAndSend("/topic/matchings/" + match.getPersonB(), toKey(match));
    }

    private String toKey(Match match) {
        return match.getPersonA() + ":" + match.getPersonB();
    }

    private Match fromKey(String matchKey) {
        var parts = matchKey.split(":");
        return new Match(parts[0], parts[1]);
    }
}
