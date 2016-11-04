package edu.kafka.storage.web;

import edu.kafka.storage.domain.Record;
import edu.kafka.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class StorageController {

    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping(path = "/store")
    public List<Record> store() {
        return storageService.store();
    }

    @GetMapping
    public List<Record> get() {
        return storageService.get();
    }
}

