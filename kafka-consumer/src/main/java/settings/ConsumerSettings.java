package settings;

import exceptions.KafkaConsumerException;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

public class ConsumerSettings {
    private static final String CONSUMER_PROPERTIES = "consumer.properties";
    private HashMap<String, String> consumerSettings;
    private static Logger LOG = Logger.getLogger(ConsumerSettings.class);

    public HashMap<String, String> getConsumerSettings() {
        return consumerSettings;
    }

    public void setConsumerSettings(HashMap<String, String> consumerSettings) {
        this.consumerSettings = consumerSettings;
    }

    /**
     * The one and only instance. This is a singleton class.
     */
    private static ConsumerSettings instance = null;

    public synchronized static ConsumerSettings getInstance() {
        if (instance == null) {
            try {
                instance = new ConsumerSettings();
            } catch (Exception e) {
                new KafkaConsumerException("Error while initiating Consumer Settings.", e);
            }
        }
        return instance;
    }

    /**
     * Constructs a new ConsumerSettingsInstance.
     */
    private ConsumerSettings() {
        HashMap<String,String> keyValues = new HashMap<>();
        Properties properties = new Properties();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream(CONSUMER_PROPERTIES);
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            properties.load(inputStreamReader);

            //Save properties
            if (properties!=null) {
                for (String key: properties.stringPropertyNames()) {
                    String value = properties.getProperty(key);
                    keyValues.put(key,value);
                }
                setConsumerSettings(keyValues);
            }
        } catch (IOException e) {
            new KafkaConsumerException("Error while reading settings from properties.", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    new KafkaConsumerException("Error when closing input stream.", e);
                }
            }
            if (inputStreamReader!=null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    new KafkaConsumerException("Error when closing input stream reader.", e);
                }
            }
        }
    }
}
