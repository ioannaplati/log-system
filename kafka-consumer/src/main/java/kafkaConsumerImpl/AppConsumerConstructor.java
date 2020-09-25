package kafkaConsumerImpl;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import settings.ConsumerSettings;

import java.util.HashMap;
import java.util.Map;

/**
 * This class creates a new Kafka Consumer by setting the appropriate properties.
 */
public class AppConsumerConstructor {

    private HashMap<String,String> consumerSettings = ConsumerSettings.getInstance().getConsumerSettings();

    public KafkaConsumer constructConsumer() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerSettings.get("host")+":"+consumerSettings.get("port")); //The connection string to a Kafka cluster
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerSettings.get("groupId"));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES,"*");

        return new KafkaConsumer(props);
    }

}
