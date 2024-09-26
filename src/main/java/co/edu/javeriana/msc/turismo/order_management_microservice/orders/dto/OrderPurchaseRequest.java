package co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto;

import co.edu.javeriana.msc.turismo.order_management_microservice.dto.Customer;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.enums.Status;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.model.OrderItem;

public record OrderPurchaseRequest(
        String id,
        @NotNull(message = "The date of the purchase order is required")
        LocalDateTime creationDate,
        @NotNull(message = "The state of the purchase order is required")
        Status status,
        @NotNull(message = "The user id of the purchase order is required")
        Customer createdBy,
        @NotNull(message = "The items of the purchase order are required")
        List<OrderItem> orderItems
) {
}