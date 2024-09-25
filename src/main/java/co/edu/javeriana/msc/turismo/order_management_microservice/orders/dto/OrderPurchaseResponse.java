package co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto;

import java.time.LocalDate;
import java.util.List;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.enums.Status;

public record OrderPurchaseResponse(
        Long id,
        LocalDate creationDate,
        Status status,
        Long createdBy,
        List <OrderItemResponse> itemsDTO
) {
}