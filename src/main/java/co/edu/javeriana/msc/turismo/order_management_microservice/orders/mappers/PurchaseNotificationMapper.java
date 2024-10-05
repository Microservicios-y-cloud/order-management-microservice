package co.edu.javeriana.msc.turismo.order_management_microservice.orders.mappers;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.model.OrderPurchase;
import co.edu.javeriana.msc.turismo.order_management_microservice.queue.dtos.PurchaseItem;
import co.edu.javeriana.msc.turismo.order_management_microservice.queue.dtos.PurchaseNotification;
import co.edu.javeriana.msc.turismo.order_management_microservice.queue.repository.SuperServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class PurchaseNotificationMapper {
    @Autowired
    private SuperServiceRepository superServiceRepository;
    public  PurchaseNotification toPurchaseNotification(OrderPurchase orderPurchase) {
        var purchaseItems = orderPurchase.getOrderItems().stream()
                .map(orderItem -> new PurchaseItem(
                        orderItem.getSubtotal(),
                        orderItem.getQuantity(),
                        superServiceRepository.findById(orderItem.getServiceId()).orElseThrow()
                ))
                .toList();

        return new PurchaseNotification(
                orderPurchase.getId(),
                orderPurchase.getCreationDate(),
                orderPurchase.getLastUpdate(),
                orderPurchase.getCreatedBy(),
                orderPurchase.getOrderStatus(),
                orderPurchase.getPaymentStatus(),
                purchaseItems,
                BigDecimal.valueOf(orderPurchase.getAmount())
        );
    }
}
