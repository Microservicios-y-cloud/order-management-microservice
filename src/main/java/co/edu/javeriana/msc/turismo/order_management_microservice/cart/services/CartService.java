package co.edu.javeriana.msc.turismo.order_management_microservice.cart.services;

import co.edu.javeriana.msc.turismo.order_management_microservice.cart.dtos.CartRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.dtos.CartResponse;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.mapper.CartMapper;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.model.Cart;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.model.CartItem;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.repository.CartRepository;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseResponse;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.enums.PaymentStatus;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.enums.Status;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.model.OrderItem;
import co.edu.javeriana.msc.turismo.order_management_microservice.queue.repository.SuperServiceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final SuperServiceRepository superServiceRepository;

    public String createCart(CartRequest cartRequest) {
        for (CartItem cartItem : cartRequest.cartItems()) {
            if (cartItem.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }
            if (cartItem.getSubtotal() <= 0) {
                throw new IllegalArgumentException("Subtotal must be greater than 0");
            }
            if (!superServiceRepository.existsById(cartItem.getServiceId())) {
                throw new EntityNotFoundException("Service not found");
            } else {
                var service = superServiceRepository.findById(cartItem.getServiceId()).get();
                if (service.startDate().isAfter(LocalDateTime.now()) || service.endDate().isBefore(LocalDateTime.now())) {
                    throw new IllegalArgumentException("Service not available");
                }
            }
        }
        if (cartRequest.createdBy() == null) {
            throw new EntityNotFoundException("User not found");
        }
        var cart = cartRepository.save(cartMapper.toCart(cartRequest));
        return cart.getId();
    }

    public String updateCart(CartRequest cartRequest) {
        var cart = cartRepository.findById(cartRequest.id()).orElseThrow(
                () -> new RuntimeException("Cart not found")
        );
        for (CartItem cartItem : cartRequest.cartItems()) {
            if (!superServiceRepository.existsById(cartItem.getServiceId())) {
                throw new EntityNotFoundException("Service not found");
            }
        }
        mergerCart(cart, cartRequest);
        cartRepository.save(cart);
        return cart.getId();
    }

    public CartResponse getCart(String id) {
        return cartRepository.findById(id)
                .map(cartMapper::toCartResponse)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
    }

    public void deleteCart(String id) {
        cartRepository.deleteById(id);
    }

    public String addCartItem(String id, CartItem cartItem) {
        var cart = cartRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cart not found")
        );
        if (!superServiceRepository.existsById(cartItem.getServiceId())) {
            throw new EntityNotFoundException("Service not found");
        }
        for (CartItem i : cart.getCartItems()) {
            if (i.getServiceId() == cartItem.getServiceId()) {
                throw new IllegalArgumentException("Service already in cart");
            }
        }
        cart.getCartItems().add(cartItem);
        cart.setLastUpdate(LocalDateTime.now());
        cartRepository.save(cart);
        return cart.getId();
    }

    public void deleteCartItem(String id, Long service) {
        var cart = cartRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cart not found")
        );
        //borramos el item del carrito
        var item = cart.getCartItems().stream()
                .filter(i -> i.getServiceId().equals(service))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));
        cart.getCartItems().remove(item);
        cart.setLastUpdate(LocalDateTime.now());
        cartRepository.save(cart);
    }

    private void mergerCart(Cart cart, @Valid CartRequest cartRequest) {
        if (StringUtils.isNotBlank(cartRequest.createdBy().getId())) {
            cart.setCreatedBy(cartRequest.createdBy());
        }
        if (cartRequest.cartItems() != null && !cartRequest.cartItems().isEmpty()) {
            cart.setCartItems(cartRequest.cartItems());
        }
    }


    public OrderPurchaseRequest toOrderRequest(CartResponse cartRequest) {
        Double amount = cartRequest.cartItems().stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();

        // Convertir cada CartItem a OrderItem usando el método toOrderItem
        List<OrderItem> orderItems = cartRequest.cartItems().stream()
                .map(this::toOrderItem) // Convierte cada CartItem a OrderItem
                .toList(); // Convierte el Stream a List<OrderItem>

        return new OrderPurchaseRequest(
                cartRequest.id(),
                cartRequest.creationDate(),
                cartRequest.lastUpdate(),
                Status.POR_ACEPTAR,
                PaymentStatus.RECHAZADA,
                cartRequest.createdBy(),
                orderItems,  // Pasar la lista de OrderItems en lugar de CartItems
                amount
        );
    }


    public OrderItem toOrderItem(CartItem cartItem) {
        return new OrderItem(
                cartItem.getSubtotal(),
                cartItem.getQuantity(),
                cartItem.getServiceId()
        );
    }

    public CartResponse getCartByUser(String userId) {
        return cartRepository.findByCreatedBy_Id(userId)
                .map(cartMapper::toCartResponse)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
    }
}
