package co.edu.javeriana.msc.turismo.order_management_microservice.cart.mapper;

import co.edu.javeriana.msc.turismo.order_management_microservice.cart.dtos.CartRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.dtos.CartResponse;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.model.Cart;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CartMapper {
    public Cart toCart(@Valid CartRequest cartRequest) {
        if (cartRequest == null) {
            return null;
        }
        return Cart.builder()
                .id(cartRequest.id())
                .creationDate(LocalDateTime.now())
                .createdBy(cartRequest.createdBy())
                .cartItems(cartRequest.cartItems())
                .build();
    }

    public CartResponse toCartResponse(Cart cart) {
        return new CartResponse(
                cart.getId(),
                cart.getCreationDate().toString(),
                cart.getLastUpdate().toString(),
                cart.getCreatedBy(),
                cart.getCartItems()
        );
    }
}
