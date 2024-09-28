package co.edu.javeriana.msc.turismo.order_management_microservice.queue.services;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.UserTransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class MessageQueueService {
    // This attribute will store the queue name,
    // which is obtained from
    // the property spring.cloud.stream.bindings.sendMessage-out-0.destination
    // from the application.yml file
    @Value("${spring.cloud.stream.bindings.sendPaymentOrder-out-0.destination}")
    private String queueName;
    @Autowired
    private StreamBridge streamBridge;
    public boolean sendPaymentOrder(UserTransactionRequest orderInformation) {
        return streamBridge.send(queueName, MessageBuilder.withPayload(orderInformation).build());
    }
}
