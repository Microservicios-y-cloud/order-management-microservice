package co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto;

public record OrderItemResponse(
        Long id,
        Double subtotal,
        Integer quantity,
        Long serviceId,
        OrderPurchaseResponse orderPurchaseDTO
) {
}