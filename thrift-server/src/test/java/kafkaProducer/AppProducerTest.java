package kafkaProducer;

import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.serializer.JsonSerializer;
import thriftGenerated.LoggingEvent;

import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppProducerTest {
    private final String TOPIC = "test-topic";
    private final String KEY = "test-key";

    private AppProducer appProducer;
    private MockProducer mockProducer;

    private void buildMockProducer(boolean autoComplete) {
        this.mockProducer = new MockProducer(autoComplete, new StringSerializer(), new JsonSerializer());
    }

    @Test
    void givenKeyValue_whenSend_thenVerifyHistory() throws ExecutionException, InterruptedException {
        buildMockProducer(true);
        //WHEN
        appProducer = new AppProducer(mockProducer);
        Future<RecordMetadata> recordMetadataFuture = appProducer.send(TOPIC, KEY, loggingEvent());
        //THEN
        assertTrue(mockProducer.history().size() == 1);
        assertTrue(recordMetadataFuture.get().topic().equalsIgnoreCase(TOPIC));
        assertTrue(recordMetadataFuture.get().partition() == 0);
    }

    @Test
    void givenKeyValue_whenSend_thenReturnException() {
        buildMockProducer(false);
        //WHEN
        appProducer = new AppProducer(mockProducer);
        Future<RecordMetadata> recordMetadataFuture = appProducer.send(TOPIC, KEY, loggingEvent());
        RuntimeException e = new RuntimeException();
        mockProducer.errorNext(e);
        //THEN
        try {
            recordMetadataFuture.get();
        } catch (InterruptedException | ExecutionException ex) {
            assertEquals(e, ex.getCause());
        }
        assertTrue(recordMetadataFuture.isDone());
    }

    private LoggingEvent loggingEvent() {
        short version = 1;
        return new LoggingEvent(version, Instant.now().toEpochMilli(), "test message");
    }
}
