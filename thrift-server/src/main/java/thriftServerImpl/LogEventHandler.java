package thriftServerImpl;

import kafkaProducer.Producer;
import kafkaProducer.ProducerConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import thriftGenerated.LoggingEvent;
import thriftGenerated.ThriftService;

public class LogEventHandler implements ThriftService.Iface {

    private static Logger LOG = Logger.getLogger(LogEventHandler.class);
    private static KafkaProducer kafkaProducer;
    private static Producer producer;

    public LogEventHandler(){
        ProducerConstructor constructor = new ProducerConstructor();
        kafkaProducer = constructor.constructProducer();
        producer = new Producer();
    }

    /**
     * This method is responsible for sending the received logging events to Kafka.
     * @param e
     * @throws TException
     */
    @Override
    public void logRoomTemperature(LoggingEvent e) throws TException {
        LOG.info("Reached Handler");
        producer.send(kafkaProducer, e);
    }
}
