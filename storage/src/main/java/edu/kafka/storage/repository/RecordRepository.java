package edu.kafka.storage.repository;

import edu.kafka.storage.domain.Record;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends CassandraRepository<Record> {
}
