package co.edu.javeriana.msc.turismo.order_management_microservice.orders.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.enums.Status;
@NoArgsConstructor
@Data

public class OrderEvent implements Event{

    private UUID eventId=UUID.randomUUID();
    private Date eventDate=new Date();
    private OrderPurchaseRequest orderPurchaseRequest;
    private Status orderStatus;

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public Date getDate() {
        return eventDate;
    }

    public OrderEvent(OrderPurchaseRequest orderPurchaseRequest, Status orderStatus) {
        this.orderPurchaseRequest = orderPurchaseRequest;
        this.orderStatus = orderStatus;
    }
}