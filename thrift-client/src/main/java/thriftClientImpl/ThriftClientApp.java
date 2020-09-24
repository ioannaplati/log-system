package thriftClientImpl;

import org.apache.log4j.Logger;
import settings.ClientSettings;
import thriftGenerated.LoggingEvent;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The Thrift Client sends a randomly generated logging event
 * every few seconds to the Thrift Server.
 */
public class ThriftClientApp {

    private static Logger LOG = Logger.getLogger(ThriftLogEventSender.class);
    private HashMap<String, String> clientSettings = ClientSettings.getInstance().getClientSettings();
    private ThriftLogEventSender sender = new ThriftLogEventSender();

    public static void main(String[] args) {
        ThriftClientApp t = new ThriftClientApp();
        t.startApp();
    }

    private void startApp() {

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                LoggingEvent logEvent = new LogEventsGenerator().generateRandomEvent();
                sender.sendLogEvent(logEvent);
            }
        };
        Timer timer = new Timer();
        long delay = 0; // By default, 0.
        long period = Long.parseLong(clientSettings.get("taskPeriodInSec"));
        timer.schedule(task, delay, period*1000L);

        LOG.info("Starting to generate and send random events...");
    }

}
