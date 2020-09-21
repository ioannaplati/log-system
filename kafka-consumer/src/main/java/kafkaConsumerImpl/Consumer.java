package kafkaConsumerImpl;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.log4j.Logger;
import settings.ConsumerSettings;

import java.util.Arrays;
import java.util.HashMap;

/**
 * The kafka consumer listens for incoming logs
 * to a certain topic and then saves them to cassandra db.
 */
public class Consumer {

    private static Logger LOG = Logger.getLogger(Consumer.class);
    private static HashMap<String,String> consumerSettings = ConsumerSettings.getInstance().getConsumerSettings();

    public static void main(String[] args) {
        ConsumerConstructor constructor = new ConsumerConstructor();
        KafkaConsumer consumer = constructor.constructConsumer();

        final Thread mainThread = Thread.currentThread();

        //Registering a shutdown hook so we can exit cleanly
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                LOG.info("Starting to exit kafka consumer loop...");
                // The shutdownhook runs in a separate thread,
                // so the only thing we can safely do to a consumer is wake it up
                consumer.wakeup();
                try {
                    mainThread.join();
                } catch (InterruptedException e) {
                    LOG.error(e);
                }
            }
        });

        try { // Subscribe to topic
            consumer.subscribe(Arrays.asList("logs"));
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Long.parseLong(consumerSettings.get("timeoutMsForPoll")));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());

                    //Todo pass to cassandra db
                }

            }
        } catch (WakeupException e) {
            // Ignore the shutdown
        } finally {
            consumer.close();
            LOG.info("Consumer is closed.");
        }

    }

}
