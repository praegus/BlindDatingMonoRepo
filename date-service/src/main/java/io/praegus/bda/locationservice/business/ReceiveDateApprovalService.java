package io.praegus.bda.locationservice.business;

import com.example.Match;
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

    private final DateGenerationService dateGenerationService;

    @KafkaListener(topics = "date-approvals", groupId = "date")
    public void listenForDateApprovals(Match match) {
        logger.info("Received Date approval: " + match);
        dateGenerationService.generateDate(match);
    }
}
