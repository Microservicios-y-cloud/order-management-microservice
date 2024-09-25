package co.edu.javeriana.msc.turismo.order_management_microservice.orders.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import co.edu.javeriana.msc.turismo.order_management_microservice.enums.Estado;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document

public class OrderPurchase {

    @Id
    private String idOrderPurchase;
    @CreatedDate
    private LocalDateTime creationDate;
    @CreatedBy
    private String createdBy;
    private Estado estado;
    private List <OrderItem> orderItems;

    public OrderPurchase(LocalDateTime creationDate, Estado estado, String createdBy) {
        this.creationDate = creationDate;
        this.estado = estado;
        this.createdBy = createdBy;
        this.orderItems = new ArrayList<>();
    }

    public Double getTotal() {
        return orderItems.stream().mapToDouble(OrderItem::getSubtotal).sum();
    }
}