package co.edu.javeriana.msc.turismo.order_management_microservice.cart.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class CartItem {
    @NotNull
    private String serviceId;
    @NotNull
    private Integer quantity;
    @NotNull
    private Double subtotal;
}
