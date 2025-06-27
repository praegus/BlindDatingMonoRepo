package io.praegus.bda.websocketservice.adapter.kafka;

import com.example.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletableFuture;

@Repository
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, Match> kafkaTemplate;

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
