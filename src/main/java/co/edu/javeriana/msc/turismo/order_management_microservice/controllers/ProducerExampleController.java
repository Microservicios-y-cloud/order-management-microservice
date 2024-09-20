package co.edu.javeriana.msc.turismo.order_management_microservice.controllers;

import co.edu.javeriana.msc.turismo.order_management_microservice.dtos.MyMessageDTO;
import co.edu.javeriana.msc.turismo.order_management_microservice.services.MessageQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/producer")
public class ProducerExampleController {
    @Autowired
    private MessageQueueService messageQueueService;
    @PostMapping("/send-simple-message")
    public ResponseEntity<String> sendSimpleMessage(@RequestBody String message) {
        messageQueueService.sendMessage(new MyMessageDTO(LocalDateTime.now(), "this is a test message"));
        log.info("Message sent: {}", message);
        return ResponseEntity.ok().body(String.format("Message sent: %s", message));
    }
}
