package co.edu.javeriana.msc.turismo.order_management_microservice.orders.mappers;

import org.springframework.stereotype.Service;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderItemRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderItemResponse;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.model.OrderItem;


@Service

public class OrderItemMapper {

    // Mapea de OrderItemRequest a OrderItem (entidad)
    public static OrderItem toOrderItem(OrderItemRequest request) {
        if (request == null) {
            return null;
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setId(request.id());
        orderItem.setSubtotal(request.subtotal());
        orderItem.setQuantity(request.quantity());
        orderItem.setServiceId(request.serviceId());
        orderItem.setOrderPurchase(OrderPurchaseMapper.toOrderPurchase(request.orderPurchaseDTO())); // Mapea el objeto anidado

        return orderItem;
    }

    // Mapea de OrderItem (entidad) a OrderItemResponse (DTO de respuesta)
    public static OrderItemResponse toOrderItemResponse(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getSubtotal(),
                orderItem.getQuantity(),
                orderItem.getServiceId(),
                OrderPurchaseMapper.toOrderPurchaseResponse(orderItem.getOrderPurchase()) // Mapea el objeto anidado
        );
    }
}