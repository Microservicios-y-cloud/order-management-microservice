package co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto;

import java.time.LocalDate;
import java.util.List;

import co.edu.javeriana.msc.turismo.order_management_microservice.enums.Estado;

public record OrderPurchaseResponse(
        Long id,
        LocalDate creationDate,
        Estado estado,
        Long createdBy,
        Long payId,
        List <OrderItemResponse> itemsDTO
) {
}