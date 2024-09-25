package co.edu.javeriana.msc.turismo.order_management_microservice.orders.mappers;

import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseResponse;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.model.OrderPurchase;

@Service
public class OrderPurchaseMapper {

    // Mapea de OrderPurchase (entidad) a OrderPurchaseResponse (DTO de respuesta)
    public static OrderPurchase toOrderPurchase(OrderPurchaseRequest request) {
        if (request == null) {
            return null;
        }

        OrderPurchase orderPurchase = new OrderPurchase();
        orderPurchase.setIdOrderPurchase(request.id());
        orderPurchase.setCreationDate(request.creationDate());
        orderPurchase.setStatus(request.status());
        orderPurchase.setCreatedBy(request.createdBy());
        orderPurchase.setItems(request.itemsDTO().stream() // Convierte la lista de OrderItemRequest a OrderItem
                .map(OrderItemMapper::toOrderItem)
                .collect(Collectors.toList())
        );

        return orderPurchase;
    }

    public static OrderPurchaseResponse toOrderPurchaseResponse(OrderPurchase orderPurchase) {
        if (orderPurchase == null) {
            return null;
        }

        return new OrderPurchaseResponse(
                orderPurchase.getIdOrderPurchase(),
                orderPurchase.getCreationDate(),
                orderPurchase.getStatus(),
                orderPurchase.getCreatedBy(),
                orderPurchase.getItems().stream() // Convierte la lista de OrderItem a OrderItemResponse
                        .map(OrderItemMapper::toOrderItemResponse)
                        .collect(Collectors.toList())
        );
    }
}