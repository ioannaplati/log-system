package thriftClientImpl;

import exceptions.ThriftClientException;
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

    private TTransport transport;

    public ThriftLogEventSender() {
        transport = new TSocket(clientSettings.get("host"), Integer.parseInt(clientSettings.get("port")));
    }

    public void sendLogEvent(LoggingEvent event) {
        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            ThriftService.Client client = new ThriftService.Client(protocol);

            client.logRoomTemperature(event);

            transport.close();
        } catch (TException e) {
            new ThriftClientException("Exception occurred while sending log event.", e);
        }
    }
}
