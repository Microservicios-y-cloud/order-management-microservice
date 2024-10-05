package co.edu.javeriana.msc.turismo.order_management_microservice.queue.services;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseResponse;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.UserTransactionRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.model.OrderPurchase;
import co.edu.javeriana.msc.turismo.order_management_microservice.queue.dtos.PurchaseNotification;
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

    @Value("${spring.cloud.stream.bindings.sendOrderNotification-out-0.destination}")
    private String notificationQueueName;

    @Autowired
    private StreamBridge streamBridge;
    public boolean sendPaymentOrder(UserTransactionRequest orderInformation) {
        return streamBridge.send(queueName, MessageBuilder.withPayload(orderInformation).build());
    }

    public boolean sendOrderNotification(PurchaseNotification purchaseNotification) {
        return streamBridge.send(notificationQueueName, MessageBuilder.withPayload(purchaseNotification).build());
    }
}
