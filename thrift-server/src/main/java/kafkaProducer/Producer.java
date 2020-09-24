package kafkaProducer;

import org.apache.kafka.clients.producer.*;
import org.apache.log4j.Logger;
import settings.ServerSettings;
import thriftGenerated.LoggingEvent;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class Producer  {

    private static Logger LOG = Logger.getLogger(Producer.class);
    private HashMap<String,String> serverSettings = ServerSettings.getInstance().getServerSettings();

    public void send(KafkaProducer producer, LoggingEvent event) {
        try {
            //We send a message, the send() method returns a Future object,
            //and we use get() to wait on the future and see if the send() was successful or not.
            producer.send(new ProducerRecord(serverSettings.get("topic"), event)).get();
        }catch (InterruptedException | ExecutionException e) {
            LOG.error(e);
        }
    }
}
