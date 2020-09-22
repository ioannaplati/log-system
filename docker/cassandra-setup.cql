CREATE KEYSPACE IF NOT EXISTS log_system
WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 2}; 

CREATE TABLE IF NOT EXISTS log_system.log_events
(
    version int,
    time timestamp,
    message text,
    room text,
    level text,
    temperature_status text,
    PRIMARY KEY (version, time)
);