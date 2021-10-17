package example1;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ConsumerStreamer {
    Logger log = LoggerFactory.getLogger(ConsumerStreamer.class);
    public static final int WAIT_TIME = 100;
    public static final String TOPIC = ProducerDemo.TOPIC;

    final KafkaConsumer<String, String> consumer;

    public ConsumerStreamer(String group) {
        consumer = KafkaFactory.consumer(group);
        consumer.subscribe(Arrays.asList(TOPIC));
    }

    public Stream<ConsumerRecord<String, String>> load() {
        return nextRecords().stream();
    }

    public void close() {
        consumer.close();
    }

    List<ConsumerRecord<String, String>> nextRecords() {
        List<ConsumerRecord<String, String>> records = new ArrayList<>();
        ConsumerRecords<String, String> polledRecords = consumer.poll(Duration.ofMillis(WAIT_TIME));
        for (ConsumerRecord<String, String> record : polledRecords) {
            records.add(record);
        }
        return records;
    }
}
