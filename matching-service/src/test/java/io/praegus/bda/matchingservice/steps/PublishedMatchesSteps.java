package io.praegus.bda.matchingservice.steps;

import com.example.Match;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PublishedMatchesSteps {
    @Autowired
    private EmbeddedKafkaBroker broker;
    @Autowired
    private Environment environment;
    private Consumer<String, Match> consumer;

    @Before
    public void listenForMatches() {
        var properties = KafkaTestUtils.consumerProps("matches-" + UUID.randomUUID(), "false", broker);
        properties.put("key.deserializer", StringDeserializer.class);
        properties.put("value.deserializer", KafkaAvroDeserializer.class);
        properties.put("schema.registry.url", environment.getRequiredProperty("spring.kafka.properties.schema.registry.url"));
        properties.put("specific.avro.reader", true);
        consumer = new DefaultKafkaConsumerFactory<String, Match>(properties).createConsumer();
        broker.consumeFromAnEmbeddedTopic(consumer, true, "matchings");
    }

    @Then("the following matches are published to kafka")
    public void matchesArePublished(DataTable table) {
        var expected = table.asMaps().stream()
                .map(row -> List.of(row.get("personA"), row.get("personB")))
                .toList();
        var actual = new ArrayList<List<String>>();
        long deadline = System.nanoTime() + Duration.ofSeconds(10).toNanos();
        do {
            consumer.poll(Duration.ofMillis(200)).forEach(record -> actual.add(List.of(
                    record.value().getPersonA().toString(), record.value().getPersonB().toString())));
        } while (actual.size() < expected.size() && System.nanoTime() < deadline);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @After
    public void closeConsumer() {
        if (consumer != null) consumer.close();
    }
}
