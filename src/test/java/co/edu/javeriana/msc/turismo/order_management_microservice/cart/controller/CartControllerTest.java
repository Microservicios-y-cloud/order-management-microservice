package co.edu.javeriana.msc.turismo.order_management_microservice.cart.controller;

import co.edu.javeriana.msc.turismo.order_management_microservice.cart.dtos.CartRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.dtos.CartResponse;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.model.CartItem;
import co.edu.javeriana.msc.turismo.order_management_microservice.dto.Customer;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CartControllerTest {

    @Autowired
    private CartController cartController;

    private CartRequest createSampleCartRequest() {
        LocalDateTime now = LocalDateTime.now();
        Customer customer = new Customer("122", "customer", "Juan", "Diego", "Echeverry", "juan_echeverry@javeriana.edu.co");
        List<CartItem> items = List.of(new CartItem(1L, 2, 100.0));
        return new CartRequest("carrito", now, now, customer, items);
    }

    @Test
    void createCart() {
        CartRequest cartRequest = createSampleCartRequest();
        ResponseEntity<String> response = cartController.createCart(cartRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void getCart() {
        CartRequest cartRequest = createSampleCartRequest();
        cartController.createCart(cartRequest);

        ResponseEntity<CartResponse> response = cartController.getCart("carrito");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("carrito", response.getBody().id());
    }

    @Test
    void getCartByUser() {
        CartRequest cartRequest = createSampleCartRequest();
        cartController.createCart(cartRequest);

        ResponseEntity<CartResponse> response = cartController.getCartByUser("Juan");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("carrito", response.getBody().id());
    }

    @Test
    void deleteCart() {
        CartRequest cartRequest = createSampleCartRequest();
        cartController.createCart(cartRequest);

        ResponseEntity<Void> response = cartController.deleteCart("carrito");

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void addCartItem() {
        CartRequest cartRequest = createSampleCartRequest();
        cartController.createCart(cartRequest);

        CartItem newItem = new CartItem(2L, 1, 50.0);
        ResponseEntity<String> response = cartController.addCartItem("carrito", newItem);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Item añadido al carrito", response.getBody());
    }

    @Test
    void deleteCartItem() {
        CartRequest cartRequest = createSampleCartRequest();
        cartController.createCart(cartRequest);

        ResponseEntity<Void> response = cartController.deleteCartItem("carrito", 1L);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void purchase() {
        CartRequest cartRequest = createSampleCartRequest();
        cartController.createCart(cartRequest);

        ResponseEntity<OrderPurchaseRequest> response = cartController.purchase("carrito");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }
}
