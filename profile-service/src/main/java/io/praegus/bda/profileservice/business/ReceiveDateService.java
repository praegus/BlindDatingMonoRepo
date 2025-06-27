package io.praegus.bda.profileservice.business;

import com.example.matching.ScheduledDate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReceiveDateService {

    Logger logger = LoggerFactory.getLogger(ReceiveDateService.class);
    private final ProfileService profileService;

    @KafkaListener(topics = "dates", groupId = "profile")
    public void listenForDates(ScheduledDate scheduledDate) {
        logger.info("Received Date approval: " + scheduledDate);
        profileService.addDateToProfile(scheduledDate.getPersonA().toString(), scheduledDate);
        profileService.addDateToProfile(scheduledDate.getPersonB().toString(), scheduledDate);
    }
}
