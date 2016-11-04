#useful kafka commands
bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic kswc-events

bin/kafka-topics.sh --zookeeper localhost:2181 --alter --topic kswc-events --partitions 3

bin/kafka-console-consumer.sh --zookeeper localhost:2181 --property key.deserializer=org.apache.kafka.streams.kstream.internals.WindowedDeserializer --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer --from-beginning --formatter kafka.tools.DefaultMessageFormatter --property print.key=true --property print.value=true --topic kswc-events-windowed