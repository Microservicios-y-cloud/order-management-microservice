package co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto;

import java.time.LocalDateTime;
import java.util.List;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.enums.PaymentStatus;
import co.edu.javeriana.msc.turismo.order_management_microservice.dto.Customer;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.enums.Status;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.model.OrderItem;

public record OrderPurchaseResponse(
        String id,
        LocalDateTime creationDate,
        LocalDateTime lastUpdate,
        Status orderstatus,
        PaymentStatus paymentStatus,
        Customer createdBy,
        List <OrderItem> orderItems,
        Double amount
) {
}