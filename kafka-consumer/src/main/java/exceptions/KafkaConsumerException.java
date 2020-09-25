package exceptions;


import org.apache.log4j.Logger;

public class KafkaConsumerException extends RuntimeException {
    private static Logger LOG = Logger.getLogger(KafkaConsumerException.class);

    public  KafkaConsumerException(String msg) {
        this(msg, null);
    }

    public KafkaConsumerException(String msg, Throwable th) {
        super(msg, th);
        LOG.error(msg, th);
    }
}
