package co.edu.javeriana.msc.turismo.order_management_microservice.orders.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.enums.Status;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@SuperBuilder

public class OrderPurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idOrderPurchase;
    private LocalDate creationDate;
    private Status status;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private Long createdBy;


    @OneToMany(mappedBy = "orderPurchase", cascade = CascadeType.ALL)
    private List <OrderItem> items;


    public OrderPurchase(LocalDate creationDate, Status status, Long createdBy) {
        this.creationDate = creationDate;
        this.status = status;
        this.createdBy = createdBy;
        this.items = new ArrayList<>();
    }

    public Double getTotal() {
        return items.stream().mapToDouble(OrderItem::getSubtotal).sum();
    }
}