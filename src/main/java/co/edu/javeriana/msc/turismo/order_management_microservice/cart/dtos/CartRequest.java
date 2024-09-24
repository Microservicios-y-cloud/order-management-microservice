package co.edu.javeriana.msc.turismo.order_management_microservice.cart.dtos;

import co.edu.javeriana.msc.turismo.order_management_microservice.cart.model.CartItem;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CartRequest(
        String id,
        @NotNull(message = "The field creationDate is mandatory")
        String createdBy,
        @NotNull(message = "The field cartItems is mandatory")
        List<CartItem> cartItems
) {
}
