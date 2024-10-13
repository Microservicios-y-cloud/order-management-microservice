package co.edu.javeriana.msc.turismo.order_management_microservice.orders.mappers;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.model.OrderPurchase;
import co.edu.javeriana.msc.turismo.order_management_microservice.queue.dtos.PurchasedInformation;
import co.edu.javeriana.msc.turismo.order_management_microservice.queue.dtos.PurchasedItem;
import co.edu.javeriana.msc.turismo.order_management_microservice.queue.repository.SuperServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class PurchasedInformationMapper {
@Autowired
private SuperServiceRepository superServiceRepository;


public PurchasedInformation toPurchasedInformation(OrderPurchase orderPurchase) {
    var purchasedItems = orderPurchase.getOrderItems().stream()
            .map(orderItem -> new PurchasedItem(
                    orderItem.getSubtotal(),
                    orderItem.getQuantity(),
                    superServiceRepository.findById(orderItem.getServiceId()).orElseThrow(),
                    orderPurchase.getOrderStatus(),
                    orderPurchase.getPaymentStatus()
            ))
            .collect(Collectors.toList());

    return new PurchasedInformation(
            orderPurchase.getCreatedBy().getId(),
            orderPurchase.getCreationDate(),
            orderPurchase.getLastUpdate(),
            orderPurchase.getCreatedBy(),
            purchasedItems,
            BigDecimal.valueOf(orderPurchase.getAmount())
    );
}    
}
