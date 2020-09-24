import kafkaConsumerImpl.Consumer;
import kafkaConsumerImpl.ConsumerConstructor;
import org.apache.log4j.Logger;
import settings.ConsumerSettings;

import java.util.HashMap;

public class KafkaConsumerApp {

    private static final Logger LOG = Logger.getLogger(Consumer.class);
    private static final HashMap<String, String> consumerSettings = ConsumerSettings.getInstance().getConsumerSettings();

    public static void main(String[] args) {
        ConsumerConstructor constructor = new ConsumerConstructor();
        Consumer consumer = new Consumer(constructor);
        consumer.startBySubscribing(consumerSettings.get("topic"));
    }
}
