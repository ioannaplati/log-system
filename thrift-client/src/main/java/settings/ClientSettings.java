package settings;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

public class ClientSettings {
    private static final String CLIENT_PROPERTIES = "client.properties";
    private HashMap<String, String> clientSettings;
    private static Logger LOG = Logger.getLogger(ClientSettings.class);

    public HashMap<String, String> getClientSettings() {
        return clientSettings;
    }

    public void setClientSettings(HashMap<String, String> clientSettings) {
        this.clientSettings = clientSettings;
    }

    /**
     * The one and only instance. This is a singleton class.
     */
    private static ClientSettings instance = null;

    public synchronized static ClientSettings getInstance() {
        if (instance == null) {
            try {
                instance = new ClientSettings();
            } catch (Exception e) {
                LOG.error(e);
            }
        }
        return instance;
    }

    /**
     * Constructs a new ClientSettingsInstance.
     */
    private ClientSettings() {
        HashMap<String,String> keyValues = new HashMap<String,String>();
        Properties properties = new Properties();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream(CLIENT_PROPERTIES);
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            properties.load(inputStreamReader);

            //Save properties
            if (properties!=null) {
                for (String key: properties.stringPropertyNames()) {
                    String value = properties.getProperty(key);
                    keyValues.put(key,value);
                }
                setClientSettings(keyValues);
            }
        } catch (IOException e) {
            LOG.error(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOG.error(e);
                }
            }
            if (inputStreamReader!=null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    LOG.error(e);
                }
            }
        }
    }
}
