package co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto;

import java.time.LocalDateTime;
import java.util.List;

import co.edu.javeriana.msc.turismo.order_management_microservice.enums.Estado;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.model.OrderItem;

public record OrderPurchaseResponse(
        String id,
        LocalDateTime creationDate,
        Estado estado,
        String createdBy,
        List <OrderItem> orderItems
) {
}