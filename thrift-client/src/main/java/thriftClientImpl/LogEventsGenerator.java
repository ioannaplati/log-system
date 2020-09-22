package thriftClientImpl;

import thriftGenerated.*;
import settings.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Random;

public class LogEventsGenerator {

    private HashMap<String, String> clientSettings = ClientSettings.getInstance().getClientSettings();

    public LoggingEvent generateRandomEvent() {
        short version = Short.parseShort(clientSettings.get("eventVersion"));
        long time = Instant.now().toEpochMilli();

        // Generate random room
        Room room = Room.findByValue(new Random().nextInt(4));

        // Generate random room temperature
        int temp = new Random().nextInt(50);
        Temperature temperature = getTemperature(temp);

        // Set level and message based on temperature
        Level logLevel = getLevel(temperature);

        String message = "Reporting from room: " + room.toString().toLowerCase() + ". Temperature is: " + temp + " celsius.";

        // Build the logging event
        LoggingEvent logEvent = new LoggingEvent();
        logEvent.setV(Short.parseShort(clientSettings.get("eventVersion")));
        logEvent.setTime(time);
        logEvent.setM(message);
        logEvent.setRoom(room);
        logEvent.setLevel(logLevel);
        logEvent.setTemperature(temperature);

        return new LoggingEvent(logEvent);
    }

    private Temperature getTemperature(int t) {
        if (t < 15) {
            return Temperature.TOO_COLD;
        } else if (t < 20) {
            return Temperature.A_BIT_COLD;
        } else if (t < 25) {
            return Temperature.ROOM_TEMP;
        } else if (t < 29) {
            return Temperature.A_BIT_WARM;
        } else {
            return Temperature.TOO_WARM;
        }
    }

    private Level getLevel(Temperature t) {
        switch (t) {
            case TOO_COLD:
            case TOO_WARM:
                return Level.ERROR;
            case A_BIT_COLD:
            case A_BIT_WARM:
                return Level.WARN;
            case ROOM_TEMP:
                return Level.INFO;
            default:
                return null;
        }
    }
}
