package thriftServerImpl;

import exceptions.ThriftServerException;
import kafkaProducer.AppProducer;
import kafkaProducer.AppProducerConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.log4j.Logger;
import settings.ServerSettings;
import thriftGenerated.LoggingEvent;
import thriftGenerated.ThriftService;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class LogEventHandler implements ThriftService.Iface {

    private static Logger LOG = Logger.getLogger(LogEventHandler.class);
    private HashMap<String,String> serverSettings = ServerSettings.getInstance().getServerSettings();

    private static KafkaProducer kafkaProducer;
    private static AppProducer appProducer;

    public LogEventHandler(){
        AppProducerConstructor constructor = new AppProducerConstructor();
        kafkaProducer = constructor.constructProducer();
        appProducer = new AppProducer(kafkaProducer);
    }

    /**
     * This method is responsible for sending the received logging events to Kafka.
     * @param e the logging event
     * @throws InterruptedException, ExecutionException ex
     */
    @Override
    public void logRoomTemperature(LoggingEvent e) {
        //We send a message, the send() method returns a Future object,
        //and we use get() to wait on the future and see if the send() was successful or not.
        try {
            String topic = serverSettings.get("topic");
            String key = serverSettings.get("key");

            appProducer.send(topic, key, e).get();
            LOG.info("Message sent from kafka producer.");
        } catch (InterruptedException | ExecutionException ex) {
            new ThriftServerException("Error occurred when sending the message from the kafka producer.", ex);
        }
    }
}
