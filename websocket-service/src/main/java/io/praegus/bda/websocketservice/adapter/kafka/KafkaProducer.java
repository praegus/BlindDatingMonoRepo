package io.praegus.bda.websocketservice.adapter.kafka;

import com.example.Match;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Match> kafkaTemplate;

    public void produceDateApproval(Match match) {
        kafkaTemplate.send("date-approvals", match)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        System.out.println("Sent message=[" + match +
                                "] with offset=[" + result.getRecordMetadata().offset() + "]");
                    } else {
                        System.out.println("Unable to send message=[" +
                                match + "] due to : " + ex.getMessage());
                    }
                });
    }
}
