## Aggregation / Ingestion System For Logging Events.
This system consists of 3 modules:
- <b>Thrift-client</b>, the module that sends random logging events to the thrift-server.
- <b>Thrift-server</b>, the module that implements the other end of the thrift API and sends the logging events to the kafka-consumer.
- <b>Kafka-consumer</b>, the module that reads events from Kafka and writes them to Cassandra DB.

### Logging Event
The file <b>loggingEvent.thrift</b> was used to generate the LoggingEvent entity and the service interface for the thrift API. \
<b>Logging Event Attributes:</b>
- v, type i16, schema version
- time, type i64, time of event
- m, type string, message
- room, type Room, the room the log is referring to
- level, type Level, level of log's importance 
- temperature, type Temperature, the temperature of the room

### Cassandra Schema
The logging events are saved in the table (column family) "log_events"
which contains the following columns:
- version (int)
- time (timestamp)
- message (text)
- room (text)
- level (text)
- temperature_status (text)

### Set up Cassandra Schema using Docker 
Create the required containers using bitnami images for Apache Kafka and Cassandra (use docker/docker-compose.yaml).
In order to create the Cassandra schema first copy the docker/cassandra-setup.cql file inside the cassandra container:
```
docker cp cassandra-setup.cql cassandra:/cassandra-setup.cql
```
Then, run:
```
docker exec -it cassandra cqlsh -u cassandra -p cassandra -f cassandra-setup.cql
```

### Running the applications
- Start Apache Kafka and Cassandra by running the following command from folder "docker". The <service_name> should be "zookeeper-server", "kafka-server", "cassandra-db" respectively.
```
docker-compose up -d <service_name>
```
- To produce the .jar files for each module run from the parent folder:
```
mvn clean compile assembly:single
```
- The produced executables should be found under "target" folder of each application.
- Run the appropriate shell script for module thrift-server, kafka-consumer and thrift-client respectively.
    
