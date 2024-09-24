package co.edu.javeriana.msc.turismo.order_management_microservice.cart.services;

import co.edu.javeriana.msc.turismo.order_management_microservice.cart.dtos.CartRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.dtos.CartResponse;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.mapper.CartMapper;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.model.Cart;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.model.CartItem;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.repository.CartRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    public String createCart(CartRequest cartRequest) {
        var cart = cartRepository.save(cartMapper.toCart(cartRequest));
        return cart.getId();
    }

    public String updateCart(CartRequest cartRequest) {
        var cart = cartRepository.findById(cartRequest.id()).orElseThrow(
                () -> new RuntimeException("Cart not found")
        );
      mergerCart(cart, cartRequest);
        cartRepository.save(cart);
        return cart.getId();
    }

    public CartResponse getCart(String id) {
        return cartRepository.findById(id)
                .map(cartMapper::toCartResponse)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    public void deleteCart(String id) {
        cartRepository.deleteById(id);
    }

    public String addCartItem(String id, CartItem cartItem) {
        var cart = cartRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Cart not found")
        );
        cart.getCartItems().add(cartItem);
        cart.setLastUpdate(LocalDateTime.now());
        cartRepository.save(cart);
        return cart.getId();
    }

    public String deleteCartItem(String id, CartRequest cartRequest) {
        var cart = cartRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Cart not found")
        );
        cart.getCartItems().removeAll(cartRequest.cartItems());
        cart.setLastUpdate(LocalDateTime.now());
        cartRepository.save(cart);
        return cart.getId();
    }

    private void mergerCart(Cart cart, @Valid CartRequest cartRequest) {
        if (StringUtils.isNotBlank(cartRequest.createdBy())) {
            cart.setCreatedBy(cartRequest.createdBy());
        }
        if (cartRequest.cartItems() != null && !cartRequest.cartItems().isEmpty()) {
            cart.setCartItems(cartRequest.cartItems());
        }
    }
}
