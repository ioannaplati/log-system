package cassandraConnector;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.apache.log4j.Logger;
import settings.ConsumerSettings;

import java.util.HashMap;

public class CassandraConnector {

    private static Logger LOG = Logger.getLogger(CassandraConnector.class);
    private static HashMap<String,String> consumerSettings = ConsumerSettings.getInstance().getConsumerSettings();

    private Cluster cluster;
    private Session session;

    public CassandraConnector() {
        Cluster.Builder b = Cluster.builder()
                .addContactPoint(consumerSettings.get("address"))
                .withPort(Integer.parseInt(consumerSettings.get("cassandraPort")))
                .withCredentials("cassandra","cassandra");

        cluster = b.build();
        session = cluster.connect(consumerSettings.get("keyspace"));
        LOG.info("Session created.");
    }

    public Session getSession() {
        return this.session;
    }

    public void close () {
        LOG.info("Closing session and cluster.");
        session.close();
        cluster.close();
    }
}
