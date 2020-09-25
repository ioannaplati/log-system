package exceptions;

import org.apache.log4j.Logger;

public class ThriftClientException extends RuntimeException {

    private static Logger LOG = Logger.getLogger(ThriftClientException.class);

    public ThriftClientException(String msg) {
        this(msg, null);
    }

    public ThriftClientException(String msg, Throwable th) {
        super(msg, th);
        LOG.error(msg, th);
    }
}
