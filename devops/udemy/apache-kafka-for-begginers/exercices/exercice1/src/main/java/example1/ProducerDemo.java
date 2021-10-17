package example1;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerDemo {
    public static final String TOPIC = "first_topic";
    Logger log = LoggerFactory.getLogger(ProducerDemo.class);
    final KafkaProducer<String, String> producer;

    public ProducerDemo() {
        producer = KafkaFactory.producer();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ProducerDemo app = new ProducerDemo();
        try {
            app.sendMessage("sample");
        } finally {
            app.close();
        }
    }

    void sendMessage(String message) throws ExecutionException, InterruptedException {
        log.info("Started");
        producer.send(record(message));
        log.info("sended");
    }

    void close() {
        producer.flush();
        producer.close();
    }

    ProducerRecord<String, String> record(String message) {
        return new ProducerRecord<>(TOPIC, message);
    }

    ProducerRecord<String, String> record(String key, String message) {
        return new ProducerRecord<>(TOPIC, key, message);
    }

    Callback callback(String key, String message) {
        return (recordMetadata, e) -> {
            if (e != null) {
                log.error("could not send message {}", e);
            } else {
                log.info("Message sended to partition {} key {}", recordMetadata.partition(), key);
            }
        };
    }
}
