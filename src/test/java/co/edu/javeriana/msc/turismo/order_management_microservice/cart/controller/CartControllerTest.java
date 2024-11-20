package co.edu.javeriana.msc.turismo.order_management_microservice.cart.controller;

import co.edu.javeriana.msc.turismo.order_management_microservice.cart.dtos.CartRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.dtos.CartResponse;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.model.CartItem;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.services.CartService;
import co.edu.javeriana.msc.turismo.order_management_microservice.dto.Customer;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.enums.PaymentStatus;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.enums.Status;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.model.OrderItem;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.services.OrderPurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private OrderPurchaseService orderPurchaseService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearCarrito() {
        LocalDateTime creationTime = LocalDateTime.now();
        LocalDateTime lastUpdateTime = LocalDateTime.now();
        Customer customer = new Customer("122", "customer", "juand", "diego", "echeverry", "juan_echeverry@javeriana.edu.co");
        List<CartItem> cartItems = List.of(new CartItem(1L, 2, 100.0));

        CartRequest cartRequest = new CartRequest("carrito", creationTime, lastUpdateTime, customer, cartItems);

        when(cartService.createCart(cartRequest)).thenReturn("Carrito creado");

        ResponseEntity<String> response = cartController.createCart(cartRequest);

        assertEquals("Carrito creado", response.getBody());
        verify(cartService, times(1)).createCart(cartRequest);
    }

    @Test
    void actualizarCarrito() {
        LocalDateTime creationTime = LocalDateTime.now();
        LocalDateTime lastUpdateTime = LocalDateTime.now();
        Customer customer = new Customer("122", "customer", "juand", "diego", "echeverry", "juan_echeverry@javeriana.edu.co");
        List<CartItem> cartItems = List.of(new CartItem(1L, 2, 100.0));

        CartRequest cartRequest = new CartRequest("carrito", creationTime, lastUpdateTime, customer, cartItems);

        when(cartService.updateCart(cartRequest)).thenReturn("Carrito actualizado");

        ResponseEntity<String> response = cartController.updateCart(cartRequest);

        assertEquals("Carrito actualizado", response.getBody());
        verify(cartService, times(1)).updateCart(cartRequest);
    }

    @Test
    void procesarCompra() {
        String cartId = "carrito";
        LocalDateTime creationTime = LocalDateTime.now();
        LocalDateTime lastUpdateTime = LocalDateTime.now();
        Customer customer = new Customer("122", "customer", "juand", "diego", "echeverry", "juan_echeverry@javeriana.edu.co");
        List<CartItem> cartItems = List.of(new CartItem(1L, 2, 100.0));
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> new OrderItem(cartItem.getSubtotal(), cartItem.getQuantity(), cartItem.getServiceId()))
                .toList();
        Double totalAmount = cartItems.stream().mapToDouble(CartItem::getSubtotal).sum();

        CartResponse cartResponse = new CartResponse(
                cartId,
                creationTime,
                lastUpdateTime,
                customer,
                cartItems
        );

        Status orderStatus = Status.ACEPTADA;
        PaymentStatus paymentStatus = PaymentStatus.ACEPTADA;

        OrderPurchaseRequest expectedOrderPurchaseRequest = new OrderPurchaseRequest(
                cartId,
                creationTime,
                lastUpdateTime,
                orderStatus,
                paymentStatus,
                customer,
                orderItems,
                totalAmount
        );

        when(cartService.getCart(cartId)).thenReturn(cartResponse);
        when(cartService.toOrderRequest(cartResponse)).thenReturn(expectedOrderPurchaseRequest);

        ResponseEntity<OrderPurchaseRequest> response = cartController.purchase(cartId);

        assertEquals(expectedOrderPurchaseRequest, response.getBody());
        verify(cartService, times(1)).getCart(cartId);
        verify(cartService, times(1)).toOrderRequest(cartResponse);
        verify(orderPurchaseService, times(1)).createOrderPurchase(expectedOrderPurchaseRequest);
    }

    @Test
    void enviarSolicitudDeCompra() {
        String cartId = "carrito";
        LocalDateTime creationTime = LocalDateTime.now();
        LocalDateTime lastUpdateTime = LocalDateTime.now();
        Customer customer = new Customer("122", "customer", "juand", "diego", "echeverry", "juan_echeverry@javeriana.edu.co");
        List<CartItem> cartItems = List.of(new CartItem(1L, 2, 100.0));
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> new OrderItem(cartItem.getSubtotal(), cartItem.getQuantity(), cartItem.getServiceId()))
                .toList();
        Double totalAmount = cartItems.stream().mapToDouble(CartItem::getSubtotal).sum();

        // Respuesta simulada del carrito
        CartResponse cartResponse = new CartResponse(
                cartId,
                creationTime,
                lastUpdateTime,
                customer,
                cartItems
        );

        Status orderStatus = Status.PAGADA;
        PaymentStatus paymentStatus = PaymentStatus.ACEPTADA;

        // Solicitud de compra esperada
        OrderPurchaseRequest expectedOrderPurchaseRequest = new OrderPurchaseRequest(
                cartId,
                creationTime,
                lastUpdateTime,
                orderStatus,
                paymentStatus,
                customer,
                orderItems,
                totalAmount
        );

        when(cartService.getCart(cartId)).thenReturn(cartResponse);
        when(cartService.toOrderRequest(cartResponse)).thenReturn(expectedOrderPurchaseRequest);

        ResponseEntity<OrderPurchaseRequest> response = cartController.purchase(cartId);

        assertEquals(expectedOrderPurchaseRequest, response.getBody());
        verify(cartService, times(1)).getCart(cartId);
        verify(cartService, times(1)).toOrderRequest(cartResponse);
        verify(orderPurchaseService, times(1)).createOrderPurchase(expectedOrderPurchaseRequest);
    }





    @Test
    void retornoDeRespuestaParaUnCarrito() {
        CartResponse cartResponse = new CartResponse(
                "carrito",
                LocalDateTime.now(),
                LocalDateTime.now(),
                new Customer("122", "sutomer", "juand", "diego", "echeverry", "juan_echeverry@javeriana.edu.co"),
                Collections.singletonList(new CartItem(1L, 2, 100.0))
        );

        when(cartService.getCart("carrito")).thenReturn(cartResponse);

        ResponseEntity<CartResponse> response = cartController.getCart("carrito");

        assertEquals(cartResponse, response.getBody());
        verify(cartService, times(1)).getCart("carrito");
    }

    @Test
    void obtenerCarritoPorUsuario() {
        CartResponse cartResponse = new CartResponse(
                "carrito",
                LocalDateTime.now(),
                LocalDateTime.now(),
                new Customer("122", "customer", "juand", "diego", "echeverry", "juan_echeverry@javeriana.edu.co"),
                Collections.singletonList(new CartItem(1L, 2, 100.0))
        );

        when(cartService.getCartByUser("juand")).thenReturn(cartResponse);

        ResponseEntity<CartResponse> response = cartController.getCartByUser("juand");

        assertEquals(cartResponse, response.getBody());
        verify(cartService, times(1)).getCartByUser("juand");
    }

    @Test
    void eliminarCarrito() {
        //El punto es que no devuelva nada
        String cartId = "carrito";

        ResponseEntity<Void> response = cartController.deleteCart(cartId);

        assertEquals(204, response.getStatusCodeValue());
        verify(cartService, times(1)).deleteCart(cartId);
    }

    @Test
    void añadirItemACarrito() {
        String cartId = "carrito";
        CartItem cartItem = new CartItem(1L, 2, 100.0);

        when(cartService.addCartItem(cartId, cartItem)).thenReturn("Item añadido");

        ResponseEntity<String> response = cartController.addCartItem(cartId, cartItem);

        assertEquals("Item añadido", response.getBody());
        verify(cartService, times(1)).addCartItem(cartId, cartItem);
    }

    @Test
    void eliminarItemDeCarrito() {
        //El pinto es que no devuelva nada
        String cartId = "carrito";
        Long cartItemId = 1L;

        ResponseEntity<Void> response = cartController.deleteCartItem(cartId, cartItemId);

        assertEquals(204, response.getStatusCodeValue());
        verify(cartService, times(1)).deleteCartItem(cartId, cartItemId);
    }


}
