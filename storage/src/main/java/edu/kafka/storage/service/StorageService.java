package edu.kafka.storage.service;

import edu.kafka.storage.domain.Record;

import java.util.List;

public interface StorageService {

    List<Record> store();

    List<Record> get();
}
