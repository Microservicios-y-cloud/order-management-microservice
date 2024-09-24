package co.edu.javeriana.msc.turismo.order_management_microservice.cart.controller;

import co.edu.javeriana.msc.turismo.order_management_microservice.cart.dtos.CartRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.dtos.CartResponse;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.services.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartControler {
    private final CartService cartService;


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
    public ResponseEntity<String> addCartItem(@PathVariable String id, @RequestBody @Valid CartRequest cartRequest) {
        return ResponseEntity.ok(cartService.addCartItem(id, cartRequest));
    }

    //delete cartItem from cart
    @DeleteMapping("/{id}/cartItem")
    public ResponseEntity<String> deleteCartItem(@PathVariable String id, @RequestBody @Valid CartRequest cartRequest) {
        return ResponseEntity.ok(cartService.deleteCartItem(id, cartRequest));
    }

}
