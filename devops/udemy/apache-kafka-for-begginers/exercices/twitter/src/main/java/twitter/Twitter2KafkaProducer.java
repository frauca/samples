package twitter;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Twitter2KafkaProducer {

    public static final String TOPIC = "twitter_tweets";
    final TwitterProducer twitterProducer;
    final KafkaProducer<String,String> kafkaProducer;

    public Twitter2KafkaProducer() {
        kafkaProducer = KafkaFactory.producer();
        twitterProducer = new TwitterProducer();
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Twitter2KafkaProducer app = new Twitter2KafkaProducer();
        try {
            app.loadTweets();
        }finally {
            app.close();
        }
    }

    void loadTweets() throws IOException, InterruptedException, ExecutionException {
        List<String> messages = twitterProducer.nextTwitts();
        for(String message:messages){
            sendMessage(message);
        }
    }

    void close(){
        kafkaProducer.flush();
        kafkaProducer.close();
    }

    void sendMessage(String message) throws ExecutionException, InterruptedException {
        kafkaProducer.send(record(message));
    }

    ProducerRecord<String, String> record(String message) {
        return new ProducerRecord<>(TOPIC, message);
    }
}
