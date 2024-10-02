package co.edu.javeriana.msc.turismo.order_management_microservice.orders.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import co.edu.javeriana.msc.turismo.order_management_microservice.dto.Customer;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.enums.PaymentStatus;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.enums.Status;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document

public class OrderPurchase {
    @Id
    private String id;
    @CreatedDate
    private LocalDateTime creationDate;
    @CreatedBy
    private Customer createdBy;
    private Status orderStatus;
    private PaymentStatus paymentStatus;
    private List <OrderItem> orderItems;
    private Double amount;

    public OrderPurchase(LocalDateTime creationDate, Status status, Customer createdBy) {
        this.creationDate = creationDate;
        this.orderStatus = status;
        this.createdBy = createdBy;
        this.orderItems = new ArrayList<>();
    }
}