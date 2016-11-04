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