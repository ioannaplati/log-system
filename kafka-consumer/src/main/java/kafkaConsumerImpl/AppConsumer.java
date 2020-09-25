package kafkaConsumerImpl;

import cassandraConnector.CassandraConnector;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.extras.codecs.jdk8.InstantCodec;
import exceptions.KafkaConsumerException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.log4j.Logger;
import settings.ConsumerSettings;
import thriftGenerated.LoggingEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

/**
 * The kafka consumer listens for incoming logs
 * to a certain topic and then saves them to cassandra db.
 */
public class AppConsumer {

    private static final Logger LOG = Logger.getLogger(AppConsumer.class);
    private static final HashMap<String, String> consumerSettings = ConsumerSettings.getInstance().getConsumerSettings();

    private static KafkaConsumer consumer;
    private static CassandraConnector connector;
    private static Thread mainThread;

    public AppConsumer(AppConsumerConstructor constructor) {
        consumer = constructor.constructConsumer();
        connector = new CassandraConnector();
        mainThread = new Thread();
        addShutDownHook();
    }

    public void startBySubscribing(String topic) {
        consume(() -> consumer.subscribe(Collections.singleton(topic)));
    }

    private void consume(Runnable beforePollingTask) {
        beforePollingTask.run();
        LOG.info("Consumer subscribed to topic " + consumerSettings.get("topic") + " and will now receive messages.");
        try {
            while (true) {
                ConsumerRecords<String, LoggingEvent> records = consumer.poll(Long.parseLong(consumerSettings.get("timeoutMsForPoll")));
                for (ConsumerRecord<String, LoggingEvent> record : records) {
                    LoggingEvent log = record.value();

                    if (log != null) {
                        Date date = new Date(log.getTime());
                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        formatter.setTimeZone(TimeZone.getTimeZone("GMT+3"));
                        String dateFormatted = formatter.format(date);

                        //Pass to cassandra db
                        Session session = connector.getSession();

                        //Codec for operation timestamp (cassandra) <-> java.time.Instant
                        session.getCluster().getConfiguration().getCodecRegistry().register(InstantCodec.instance);
                        PreparedStatement statement =
                                session.prepare("INSERT INTO log_events (version, time, message, room, level, temperature_status) VALUES (?,?,?,?,?,?)");
                        BoundStatement boundStatement =
                                statement.bind((int) log.getV(), Instant.parse(dateFormatted), log.getM(), log.getRoom().toString(), log.getLevel().toString(), log.getTemperature().toString());
                        session.execute(boundStatement);
                        LOG.info("Inserted new log in cassandra db.");
                    }
                }
            }
        } catch (WakeupException e) {
            // Ignore the shutdown
        }finally {
            connector.close();
            consumer.close();
            LOG.info("Consumer is closed.");
        }
    }

    //Registering a shutdown hook so we can exit the consumer cleanly
    private void addShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // The shutdownhook runs in a separate thread, so the only thing we can safely do to a consumer is wake it up
            LOG.info("Starting to exit kafka consumer loop...");
            consumer.wakeup();
            try {
                mainThread.join();
            } catch (InterruptedException e) {
                new KafkaConsumerException("Thread interrupted.",e);
            }
        }));
    }
}
