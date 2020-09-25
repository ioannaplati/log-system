import kafkaConsumerImpl.AppConsumer;
import kafkaConsumerImpl.AppConsumerConstructor;
import org.apache.log4j.Logger;
import settings.ConsumerSettings;

import java.util.HashMap;

public class KafkaConsumerApp {

    private static final Logger LOG = Logger.getLogger(KafkaConsumerApp.class);
    private static final HashMap<String, String> consumerSettings = ConsumerSettings.getInstance().getConsumerSettings();

    public static void main(String[] args) {
        AppConsumerConstructor constructor = new AppConsumerConstructor();
        AppConsumer appConsumer = new AppConsumer(constructor);
        appConsumer.startBySubscribing(consumerSettings.get("topic"));
    }
}
