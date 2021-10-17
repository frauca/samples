package twitter;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class TwitterProducer {


    public static void main(String[] args) throws IOException {
        TwitterProducer app = new TwitterProducer();
        try {
            app.nextTwitts();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

        }
    }

    public List<String> nextTwitts() throws IOException, InterruptedException {
        List<String> messages = new ArrayList<>();
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100000);
        Client client = client(msgQueue);
        while (messages.size() < 10) {
            String msg = msgQueue.take();
            messages.add(msg);
        }
        client.stop();
        log.info("messages {}", messages);
        return messages;
    }

    Client client(BlockingQueue msgQueue) throws IOException {
        ClientBuilder cb = new ClientBuilder()
                .name("FraucaT2K")
                .hosts(new HttpHosts(Constants.STREAM_HOST))
                .authentication(authentication())
                .endpoint(endpoint())
                .processor(new StringDelimitedProcessor(msgQueue));

        Client client = cb.build();
        client.connect();
        return client;
    }

    StatusesFilterEndpoint endpoint() {
        StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
        List<String> terms = Lists.newArrayList("covid");
        endpoint.trackTerms(terms);
        return endpoint;
    }

    Authentication authentication() throws IOException {
        Properties secrets = secrets();
        return new OAuth1(secrets.getProperty("api_key"),
                secrets.getProperty("api_secret"),
                secrets.getProperty("access_token"),
                secrets.getProperty("access_token_secret"));
    }

    Properties secrets() throws IOException {
        InputStream propertiesFile = getClass().getClassLoader().getResourceAsStream("secrets.properties");
        Properties properties = new Properties();
        properties.load(propertiesFile);
        return properties;
    }
}
