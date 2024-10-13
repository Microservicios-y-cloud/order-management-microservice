package co.edu.javeriana.msc.turismo.order_management_microservice.queue.dtos;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.enums.PaymentStatus;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.enums.Status;

public record PurchasedItem(
        Double subtotal,
        Integer quantity,
        SuperService service,
        Status orderStatus,
        PaymentStatus paymentStatus
) {
}