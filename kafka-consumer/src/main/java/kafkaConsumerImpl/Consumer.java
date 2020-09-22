package kafkaConsumerImpl;

import cassandraConnector.CassandraConnector;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.extras.codecs.jdk8.InstantCodec;
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
public class Consumer {

    private static Logger LOG = Logger.getLogger(Consumer.class);
    private static HashMap<String,String> consumerSettings = ConsumerSettings.getInstance().getConsumerSettings();

    private static ConsumerConstructor constructor;
    private static KafkaConsumer consumer;
    private static Thread mainThread = new Thread();
    private static CassandraConnector connector;
    private static Session session;

    public static void main(String[] args) {
        constructor = new ConsumerConstructor();
        consumer = constructor.constructConsumer();
        connector = new CassandraConnector();

        mainThread = Thread.currentThread();

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
            consumer.subscribe(Collections.singletonList("logs"));
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
                        session = connector.getSession();
                        session.getCluster().getConfiguration().getCodecRegistry().register(InstantCodec.instance);
                        PreparedStatement statement = session
                                .prepare("INSERT INTO log_events (version, time, message, room, level, temperature_status) VALUES (?,?,?,?,?,?)");
                        BoundStatement boundStatement = statement.bind((int)log.getV(), Instant.parse(dateFormatted),
                                log.getM(), log.getRoom().toString(), log.getLevel().toString(), log.getTemperature().toString());
                        session.execute(boundStatement);
                        LOG.info("Executed insert for " + log);
                    }

                }

            }
        } catch (WakeupException e) {
            // Ignore the shutdown
        } finally {
            connector.close();
            consumer.close();
            LOG.info("Consumer is closed.");
        }

    }

}
