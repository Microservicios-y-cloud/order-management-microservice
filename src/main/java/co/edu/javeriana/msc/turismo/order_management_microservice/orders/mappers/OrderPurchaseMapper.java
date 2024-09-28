package co.edu.javeriana.msc.turismo.order_management_microservice.orders.mappers;

import org.springframework.stereotype.Service;

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
        orderPurchase.setOrderStatus(request.orderStatus());
        orderPurchase.setPaymentStatus(request.paymentStatus());
        orderPurchase.setCreatedBy(request.createdBy());
        orderPurchase.setOrderItems(request.orderItems());
        orderPurchase.setAmount(request.amount());

        return orderPurchase;
    }

    public static OrderPurchaseResponse toOrderPurchaseResponse(OrderPurchase orderPurchase) {
        if (orderPurchase == null) {
            return null;
        }

        return new OrderPurchaseResponse(
                orderPurchase.getIdOrderPurchase(),
                orderPurchase.getCreationDate(),
                orderPurchase.getOrderStatus(),
                orderPurchase.getPaymentStatus(),
                orderPurchase.getCreatedBy(),
                orderPurchase.getOrderItems(),
                orderPurchase.getAmount()
        );
    }
}