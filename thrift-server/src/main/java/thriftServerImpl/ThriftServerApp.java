package thriftServerImpl;

import javax.annotation.PreDestroy;

/**
 * The Thrift Server implements the other end of
 * the thrift API and sends logging events to Kafka.
 */

public class ThriftServerApp {

    private ThriftServer thriftServer = new ThriftServer();

    public static void main(String[] args) {
        ThriftServerApp t = new ThriftServerApp();
        t.startServer();
    }

    private void startServer() {
        thriftServer.start();
    }

    @PreDestroy
    public void cleanUp() {
        thriftServer.stop();
    }
}
