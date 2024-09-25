package co.edu.javeriana.msc.turismo.order_management_microservice.orders.model;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class OrderItem {
    @NotNull
    private Double subtotal;
    @NotNull
    private Integer quantity;
    @NotNull
    private Long serviceId;
}