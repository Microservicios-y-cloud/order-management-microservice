package co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

import co.edu.javeriana.msc.turismo.order_management_microservice.enums.Estado;

public record OrderPurchaseRequest(
        Long id,
        @NotNull(message = "The date of the purchase order is required")
        LocalDate creationDate,
        @NotNull(message = "The state of the purchase order is required")
        Estado estado,
        @NotNull(message = "The user id of the purchase order is required")
        Long createdBy,
        @NotNull(message = "The pay id of the question is required")
        Long payId,
        @NotNull(message = "The items of the purchase order is required")
        List<OrderItemRequest> itemsDTO
) {
}