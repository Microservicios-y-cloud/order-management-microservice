package co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto;

import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(
        Long id,
        @NotNull(message = "The subtotal of the item order is required")
        Double subtotal,
        @NotNull(message = "The quantity of the purchase order is required")
        Integer quantity,
        @NotNull(message = "The service id of the purchase order is required")
        Long serviceId,
        @NotNull(message = "The purchase order id of the purchase order is required")
        OrderPurchaseRequest orderPurchaseDTO
) {
}