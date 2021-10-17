package example1;

public class ProducerDemoWithCallback extends ProducerDemo {

    public static void main(String[] args) {
        ProducerDemoWithCallback app = new ProducerDemoWithCallback();
        try {
            for (int i = 0; i < 100; i++) {
                app.sendMessage("with callback " + i);
            }

        } finally {
            app.close();
        }
    }

    void sendMessage(String message) {
        producer.send(record(message), callback(null, message));
    }
}
