package io.praegus.bda.matchingservice.business;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MatchingReceiver {

    Logger logger = LoggerFactory.getLogger(MatchingReceiver.class);
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "matchings", groupId = "ws")
    public void listenForMatches(String message) {
        logger.info("Received Message in group foo: " + message);
        messagingTemplate.convertAndSend("/topic/matchings", message);
    }
}
