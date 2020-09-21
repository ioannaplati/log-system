package thriftClientImpl;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import settings.ClientSettings;
import thriftGenerated.LoggingEvent;

import org.apache.log4j.Logger;
import thriftGenerated.ThriftService;

import java.util.HashMap;

public class ThriftLogEventSender {
    private static Logger LOG = Logger.getLogger(ThriftLogEventSender.class);
    private HashMap<String, String> clientSettings = ClientSettings.getInstance().getClientSettings();

    public void sendLogEvent(LoggingEvent event) {
        TTransport transport;
        transport = new TSocket(clientSettings.get("host"), Integer.parseInt(clientSettings.get("port")));
        try {

            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            ThriftService.Client client = new ThriftService.Client(protocol);

            LOG.debug("Calling service's log random event method...");
            client.logRoomTemperature(event);

            LOG.debug("Log event has been sent...");
            transport.close();
        } catch (TException e) {
            LOG.error(e);
        }


    }
}
