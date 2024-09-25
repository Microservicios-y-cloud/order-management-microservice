package co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto;

import java.time.LocalDateTime;
import java.util.List;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.enums.Status;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.model.OrderItem;

public record OrderPurchaseResponse(
        String id,
        LocalDateTime creationDate,
        Status status,
        String createdBy,
        List <OrderItem> orderItems
) {
}