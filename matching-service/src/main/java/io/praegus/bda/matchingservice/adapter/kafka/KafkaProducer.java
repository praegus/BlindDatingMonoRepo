package io.praegus.bda.matchingservice.adapter.kafka;

import com.example.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, Match> kafkaTemplate;

    public void sendMessage(Match match) {
        kafkaTemplate.send("matchings", match)
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
