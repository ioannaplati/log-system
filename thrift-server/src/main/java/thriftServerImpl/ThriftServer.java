package thriftServerImpl;

import exceptions.ThriftServerException;
import org.apache.log4j.Logger;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import settings.ServerSettings;
import thriftGenerated.ThriftService;

import java.util.HashMap;

/**
 * This Server uses a listening port for incoming calls
 * from the client (The Thrift API endpoint).
 */
public class ThriftServer {

    private static Logger LOG = Logger.getLogger(ThriftServer.class);
    private HashMap<String,String> serverSettings = ServerSettings.getInstance().getServerSettings();

    private TServer server;

    public void start() {
        try {
            TServerSocket serverSocket = new TServerSocket(Integer.parseInt(serverSettings.get("port")));

            //Handle incoming calls
            LogEventHandler handler = new LogEventHandler();
            ThriftService.Processor<ThriftService.Iface> processor =
                    new ThriftService.Processor<>(handler);

            server = new TThreadPoolServer(new TThreadPoolServer.Args(serverSocket).processor(processor));

            LOG.info("Thrift Server has started!");
            server.serve();
        } catch (TTransportException e) {
           new ThriftServerException("TTransport Exception occurred in Thrift Server.", e);
        }

    }

    public void stop() {
        if (server != null && server.isServing()) {
            LOG.warn("Stopping the server...");
            server.stop();
        }
    }
}
