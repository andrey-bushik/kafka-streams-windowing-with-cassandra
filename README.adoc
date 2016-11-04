= Install a local Cassandra

The simplest way to install Cassandra is via the excellent https://github.com/pcmanus/ccm[ccm] tool.

[source]
----
ccm create test -v 3.7 -n 1 -s --vnodes
----


Connect to one of the nodes:
[source]
----
ccm node1 cqlsh
----

And run these cql statements

[source]
----

CREATE KEYSPACE IF NOT EXISTS kstreams WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};

use kstreams;

CREATE TABLE IF NOT EXISTS kstreams.aggregation (
  event_name text,
  aggregation_type text, --enum (5min, hourly, daily)
  start_time timestamp,
  count bigint,
  PRIMARY KEY ((event_name, aggregation_type), start_time)
)
WITH CLUSTERING ORDER BY (start_time DESC);
----

= Install a local Kafka

Download Kafka version >= 0.10.1.0 from http://kafka.apache.org/downloads[here].

Open config/server.properties and add:

```
delete.topic.enable=true
auto.create.topics.enable=true
```

If Kafka should be available outside of your local machine add:

```
advertised.host.name=[IP_ADDRESS]
```

If more than one broker required copy server.properties to server-[1..etc].properties and modify:

```
broker.id=[1,2..etc]
log.dirs=/tmp/kafka-logs-[1,2..etc]
```

Start Kafka:

```
bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties
bin/kafka-server-start.sh config/server-1.properties
etc
```