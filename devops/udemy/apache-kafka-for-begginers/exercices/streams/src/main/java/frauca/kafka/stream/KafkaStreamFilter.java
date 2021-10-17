package frauca.kafka.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaStreamFilter {


    Logger log = LoggerFactory.getLogger(KafkaStreamFilter.class);

    public static void main(String[] args) {
        KafkaStreamFilter app = new KafkaStreamFilter();
        app.exec();
    }

    void exec(){
        log.info("nothing by now");
    }
}
