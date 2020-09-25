package kafkaProducer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import thriftGenerated.LoggingEvent;

import java.util.concurrent.Future;

public class AppProducer {

    private final org.apache.kafka.clients.producer.Producer producer;

    public AppProducer(KafkaProducer producer) {
        this.producer = producer;
    }

    // For testing purposes
    public AppProducer(Producer producer) {this.producer = producer;}

    public Future<RecordMetadata> send(String topic, String key, LoggingEvent event) {
        return producer.send(new ProducerRecord(topic, key, event));
    }
}
