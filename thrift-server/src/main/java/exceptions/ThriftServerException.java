package exceptions;

import org.apache.log4j.Logger;

public class ThriftServerException extends RuntimeException {

    private static Logger LOG = Logger.getLogger(ThriftServerException.class);

    public ThriftServerException(String msg) {
        this(msg, null);
    }

    public ThriftServerException(String msg, Throwable th) {
        super(msg, th);
        LOG.error(msg, th);
    }
}
