package co.edu.javeriana.msc.turismo.order_management_microservice.cart.dtos;

import co.edu.javeriana.msc.turismo.order_management_microservice.cart.model.CartItem;
import co.edu.javeriana.msc.turismo.order_management_microservice.dto.Customer;

import java.time.LocalDateTime;
import java.util.List;

public record CartResponse(
        String id,
        LocalDateTime creationDate,
        LocalDateTime lastUpdate,
        Customer createdBy,
        List<CartItem> cartItems) {
}
