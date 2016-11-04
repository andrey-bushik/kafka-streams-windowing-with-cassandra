package edu.kafka.consumer.web;

import edu.kafka.consumer.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ConsumerController {

    private final ConsumerService consumerService;

    @Autowired
    public ConsumerController(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @GetMapping(path = "/start")
    public String start() {
        return consumerService.start();
    }

    @GetMapping(path = "/stop")
    public String stop() {
        return consumerService.stop();
    }

    @GetMapping
    public String status() {
        return consumerService.status();
    }
}