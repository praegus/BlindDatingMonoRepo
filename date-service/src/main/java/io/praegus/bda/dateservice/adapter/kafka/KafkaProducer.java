package io.praegus.bda.dateservice.adapter.kafka;

import com.example.matching.ScheduledDate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, ScheduledDate> kafkaTemplate;

    public void produceDate(ScheduledDate message) {
        kafkaTemplate.send("dates", message)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        System.out.println("Sent message=[" + message +
                                "] with offset=[" + result.getRecordMetadata().offset() + "]");
                    } else {
                        System.out.println("Unable to send message=[" +
                                message + "] due to : " + ex.getMessage());
                    }
                });
    }
}
