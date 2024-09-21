package co.edu.javeriana.msc.turismo.order_management_microservice.orders.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@SuperBuilder

public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Double subtotal;
    private Integer quantity;

    @Column(nullable = false, updatable = false)
    private Long serviceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idOrderPurchase", referencedColumnName = "idOrderPurchase")
    private OrderPurchase orderPurchase;
}