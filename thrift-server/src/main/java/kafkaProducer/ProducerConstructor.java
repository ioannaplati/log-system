package kafkaProducer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import settings.ServerSettings;

import java.util.HashMap;
import java.util.Map;

/**
 * This class creates a new Kafka Producer by setting the appropriate properties.
 */
public class ProducerConstructor {

    private HashMap<String,String> serverSettings = ServerSettings.getInstance().getServerSettings();

    public KafkaProducer
    constructProducer() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serverSettings.get("host")+":"+serverSettings.get("producerPort"));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new KafkaProducer(props);
    }
}
