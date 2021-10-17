package twitter;

import com.google.gson.JsonParser;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;

public class Kafka2ElasticConsumer {

    public static final Duration POLL_INTERVAL = Duration.ofMillis(100);
    public static final String TOPIC = Twitter2KafkaProducer.TOPIC;
    final KafkaConsumer<String, String> kafkaConsumer;
    final ElasticSearchProducer elasticWritter;
    final JsonParser jsonParser;

    public Kafka2ElasticConsumer() {
        elasticWritter = new ElasticSearchProducer();
        kafkaConsumer = KafkaFactory.consumer("twitter");
        kafkaConsumer.subscribe(Arrays.asList(TOPIC));
        jsonParser = new JsonParser();
    }

    public static void main(String[] args) throws IOException {
        Kafka2ElasticConsumer app = new Kafka2ElasticConsumer();
        try {
            app.load();
        } finally {
            app.close();
        }
    }

    void load() throws IOException {
        for (int readed = 0; readed < 10; ) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(POLL_INTERVAL);
            for (ConsumerRecord record : records) {
                String message = record.value().toString();
                String id = extractIdFromTweet(message);
                elasticWritter.sendMessage(id, message);
                readed++;
            }
        }
    }

    void close() throws IOException {
        kafkaConsumer.close();
        elasticWritter.close();
    }

    String extractIdFromTweet(String tweetJson) {
        return jsonParser.parse(tweetJson)
                .getAsJsonObject()
                .get("id_str")
                .getAsString();
    }
}
