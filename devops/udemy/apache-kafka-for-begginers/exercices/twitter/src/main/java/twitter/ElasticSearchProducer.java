package twitter;

import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.Objects;

public class ElasticSearchProducer {
    final RestHighLevelClient client;

    public ElasticSearchProducer() {
        client = Objects.requireNonNull(client());
    }

    public static void main(String[] args) throws IOException {
        ElasticSearchProducer app = new ElasticSearchProducer();
        try {
            app.sendMessage("1", "{\"foo\":\"var\"}");
        } finally {
            app.close();
        }
    }

    public void sendMessage(String id, String message) throws IOException {
        IndexRequest request = new IndexRequest("twitter")
                .id(id)
                .source(message, XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
    }

    public void close() throws IOException {
        client.close();
    }

    RestHighLevelClient client() {
        RestClientBuilder builder = RestClient.builder(
                new HttpHost("localhost", 9200)
        );
        return new RestHighLevelClient(builder);
    }
}
