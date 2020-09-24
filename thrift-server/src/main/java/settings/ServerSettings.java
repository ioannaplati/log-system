package settings;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

public class ServerSettings {
    private static final String SERVER_PROPERTIES = "server.properties";
    private HashMap<String, String> serverSettings;
    private static Logger LOG = Logger.getLogger(ServerSettings.class);

    public HashMap<String, String> getServerSettings() {
        return serverSettings;
    }

    public void setServerSettings(HashMap<String, String> serverSettings) {
        this.serverSettings = serverSettings;
    }

    /**
     * The one and only instance. This is a singleton class.
     */
    private static ServerSettings instance = null;

    public synchronized static ServerSettings getInstance() {
        if (instance == null) {
            try {
                instance = new ServerSettings();
            } catch (Exception e) {
                LOG.error(e);
            }
        }
        return instance;
    }

    /**
     * Constructs a new ServerSettingsInstance.
     */
    private ServerSettings() {
        HashMap<String,String> keyValues = new HashMap<String,String>();
        Properties properties = new Properties();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream(SERVER_PROPERTIES);
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            properties.load(inputStreamReader);

            //Save properties
            if (properties!=null) {
                for (String key: properties.stringPropertyNames()) {
                    String value = properties.getProperty(key);
                    keyValues.put(key,value);
                }
                setServerSettings(keyValues);
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
