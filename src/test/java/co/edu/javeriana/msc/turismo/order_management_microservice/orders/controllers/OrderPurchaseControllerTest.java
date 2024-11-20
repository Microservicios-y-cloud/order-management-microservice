package co.edu.javeriana.msc.turismo.order_management_microservice.orders.controllers;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseResponse;
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

class OrderPurchaseControllerTest {

    @Mock
    private OrderPurchaseService orderPurchaseService;

    @InjectMocks
    private OrderPurchaseController orderPurchaseController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obtenerOrdenPorId() {
        String orderId = "orden123";
        OrderPurchaseResponse response = new OrderPurchaseResponse(
                orderId,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null, null, null, Collections.emptyList(), 100.0
        );

        when(orderPurchaseService.findById(orderId)).thenReturn(response);

        ResponseEntity<OrderPurchaseResponse> result = orderPurchaseController.getOrder(orderId);

        assertEquals(response, result.getBody());
        verify(orderPurchaseService, times(1)).findById(orderId);
    }

    @Test
    void obtenerTodasLasOrdenes() {
        List<OrderPurchaseResponse> orders = List.of(
                new OrderPurchaseResponse("orden123", LocalDateTime.now(), LocalDateTime.now(), null, null, null, Collections.emptyList(), 100.0)
        );

        when(orderPurchaseService.findAllOrderPurchases()).thenReturn(orders);

        ResponseEntity<List<OrderPurchaseResponse>> result = orderPurchaseController.findAll();

        assertEquals(orders, result.getBody());
        verify(orderPurchaseService, times(1)).findAllOrderPurchases();
    }

    @Test
    void obtenerOrdenesAprobadasPorUsuario() {
        String userId = "user123";
        List<OrderPurchaseResponse> orders = List.of(
                new OrderPurchaseResponse("orden123", LocalDateTime.now(), LocalDateTime.now(), null, null, null, Collections.emptyList(), 100.0)
        );

        when(orderPurchaseService.findAllPurchasedByUsers(userId)).thenReturn(orders);

        ResponseEntity<List<OrderPurchaseResponse>> result = orderPurchaseController.getPurchasedByUser(userId);

        assertEquals(orders, result.getBody());
        verify(orderPurchaseService, times(1)).findAllPurchasedByUsers(userId);
    }

    @Test
    void obtenerOrdenesPorUsuario() {
        String userId = "user123";
        List<OrderPurchaseResponse> orders = List.of(
                new OrderPurchaseResponse("orden123", LocalDateTime.now(), LocalDateTime.now(), null, null, null, Collections.emptyList(), 100.0)
        );

        when(orderPurchaseService.findAllOrdersByUsers(userId)).thenReturn(orders);

        ResponseEntity<List<OrderPurchaseResponse>> result = orderPurchaseController.getOrdersdByUser(userId);

        assertEquals(orders, result.getBody());
        verify(orderPurchaseService, times(1)).findAllOrdersByUsers(userId);
    }

    @Test
    void crearNuevaOrden() {
        OrderPurchaseRequest request = new OrderPurchaseRequest("orden123", LocalDateTime.now(), LocalDateTime.now(), null, null, null, Collections.emptyList(), 100.0);
        String createdOrderId = "orden123";

        when(orderPurchaseService.createOrderPurchase(request)).thenReturn(createdOrderId);

        ResponseEntity<String> result = orderPurchaseController.createOrder(request);

        assertEquals(createdOrderId, result.getBody());
        assertEquals(201, result.getStatusCodeValue());
        verify(orderPurchaseService, times(1)).createOrderPurchase(request);
    }

    @Test
    void actualizarEstadoDeOrden() {
        String orderId = "orden123";
        OrderPurchaseRequest request = new OrderPurchaseRequest("orden123", LocalDateTime.now(), LocalDateTime.now(), null, null, null, Collections.emptyList(), 100.0);
        OrderPurchaseResponse response = new OrderPurchaseResponse(orderId, LocalDateTime.now(), LocalDateTime.now(), null, null, null, Collections.emptyList(), 100.0);

        when(orderPurchaseService.updateOrderPurchaseEstado(orderId, request)).thenReturn(response);

        ResponseEntity<OrderPurchaseResponse> result = orderPurchaseController.updateOrderEstado(orderId, request);

        assertEquals(response, result.getBody());
        verify(orderPurchaseService, times(1)).updateOrderPurchaseEstado(orderId, request);
    }

    @Test
    void eliminarOrden() {
        String orderId = "orden123";

        ResponseEntity<Void> result = orderPurchaseController.deleteOrder(orderId);

        assertEquals(204, result.getStatusCodeValue());
        verify(orderPurchaseService, times(1)).deleteOrderPurchase(orderId);
    }
}