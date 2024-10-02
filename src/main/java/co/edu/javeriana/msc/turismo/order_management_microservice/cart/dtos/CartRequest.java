package co.edu.javeriana.msc.turismo.order_management_microservice.cart.dtos;

import co.edu.javeriana.msc.turismo.order_management_microservice.cart.model.CartItem;
import co.edu.javeriana.msc.turismo.order_management_microservice.dto.Customer;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record CartRequest(
        String id,
        LocalDateTime creationDate,
        LocalDateTime lastUpdate,
        @NotNull(message = "The field createdBy is mandatory")
        Customer createdBy,
        @NotNull(message = "The field cartItems is mandatory")
        List<CartItem> cartItems
) {
}
