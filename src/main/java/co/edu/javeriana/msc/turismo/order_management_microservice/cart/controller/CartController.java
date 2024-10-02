package co.edu.javeriana.msc.turismo.order_management_microservice.cart.controller;

import co.edu.javeriana.msc.turismo.order_management_microservice.cart.dtos.CartRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.dtos.CartResponse;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.model.CartItem;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.services.CartService;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseResponse;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.services.OrderPurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final OrderPurchaseService orderPurchaseService;


    @PostMapping
    public ResponseEntity<String> createCart(@RequestBody @Valid CartRequest cartRequest) {
        return ResponseEntity.ok(cartService.createCart(cartRequest));
    }

    @PutMapping
    public ResponseEntity<String> updateCart(@RequestBody @Valid CartRequest cartRequest) {
        return ResponseEntity.ok(cartService.updateCart(cartRequest));
    }
    @GetMapping("/{id}")
    public ResponseEntity<CartResponse> getCart(@PathVariable String id) {
        return ResponseEntity.ok(cartService.getCart(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable String id) {
        cartService.deleteCart(id);
        return ResponseEntity.accepted().build();
    }

    //add cartItem to cart
    @PostMapping("/{id}/cartItem")
    public ResponseEntity<String> addCartItem(@PathVariable String id, @RequestBody @Valid CartItem cartItem) {
        return ResponseEntity.ok(cartService.addCartItem(id, cartItem));
    }

    //delete cartItem from cart
    @DeleteMapping("/{id}/cartItem")
    public ResponseEntity<String> deleteCartItem(@PathVariable String id, @RequestBody @Valid CartRequest cartRequest) {
        return ResponseEntity.ok(cartService.deleteCartItem(id, cartRequest));
    }

    @GetMapping("/purchase/{id}")
    public ResponseEntity<OrderPurchaseRequest> purchase(@PathVariable String id) {
        CartResponse cartResponse = cartService.getCart(id);
        log.info("Datos de la compra 1: {}", cartResponse.id());
        log.info("Datos de la compra 2: {}", cartResponse.creationDate());
        log.info("Datos de la compra 3: {}", cartResponse.lastUpdate());
        log.info("Datos de la compra 4: {}", cartResponse.createdBy().getFirstName());
        log.info("Datos de la compra 5: {}", cartResponse.cartItems().get(0).getServiceId());
        //Implementar el llamado a la función que hace la lógica de pago
        OrderPurchaseRequest auxOrderPurchase = cartService.toOrderRequest(cartResponse);
        //Es para orderPurchaseRequest
        orderPurchaseService.createOrderPurchase(auxOrderPurchase);
        return ResponseEntity.ok(auxOrderPurchase);
    }
}
