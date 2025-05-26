package io.praegus.bda.matchingservice.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ReceiveDateApprovalService {

    Logger logger = LoggerFactory.getLogger(ReceiveDateApprovalService.class);
    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    private final DateGenerationService dateGenerationService;

    @KafkaListener(topics = "date-approvals", groupId = "date")
    public void listenForDateApprovals(String message) {
        logger.info("Received Date approval: " + message);
        try {
            var match = objectMapper.readValue(message, Match.class);
            dateGenerationService.generateDate(match);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
