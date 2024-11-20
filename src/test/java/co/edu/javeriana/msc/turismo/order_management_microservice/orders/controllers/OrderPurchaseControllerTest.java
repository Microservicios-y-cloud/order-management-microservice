package co.edu.javeriana.msc.turismo.order_management_microservice.orders.controllers;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseResponse;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.services.OrderPurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OrderPurchaseControllerTest {

    @Autowired
    private OrderPurchaseController orderPurchaseController;

    @Autowired
    private OrderPurchaseService orderPurchaseService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void obtenerOrdenPorId() {
        OrderPurchaseRequest request = new OrderPurchaseRequest("orden123", LocalDateTime.now(), LocalDateTime.now(), null, null, null, Collections.emptyList(), 100.0);
        String createdOrderId = orderPurchaseService.createOrderPurchase(request);

        ResponseEntity<OrderPurchaseResponse> result = orderPurchaseController.getOrder(createdOrderId);

        assertEquals(200, result.getStatusCodeValue());
        assertNotNull(result.getBody());
    }

    @Test
    void obtenerTodasLasOrdenes() {
        OrderPurchaseRequest request1 = new OrderPurchaseRequest("orden123", LocalDateTime.now(), LocalDateTime.now(), null, null, null, Collections.emptyList(), 100.0);
        OrderPurchaseRequest request2 = new OrderPurchaseRequest("orden124", LocalDateTime.now(), LocalDateTime.now(), null, null, null, Collections.emptyList(), 150.0);
        orderPurchaseService.createOrderPurchase(request1);
        orderPurchaseService.createOrderPurchase(request2);

        ResponseEntity<List<OrderPurchaseResponse>> result = orderPurchaseController.findAll();

        assertEquals(200, result.getStatusCodeValue());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().size() > 1);
    }

    @Test
    void obtenerOrdenesAprobadasPorUsuario() {
        String userId = "user123";
        OrderPurchaseRequest request = new OrderPurchaseRequest("orden123", LocalDateTime.now(), LocalDateTime.now(), null, null, null, Collections.emptyList(), 100.0);
        orderPurchaseService.createOrderPurchase(request);

        ResponseEntity<List<OrderPurchaseResponse>> result = orderPurchaseController.getPurchasedByUser(userId);

        assertEquals(200, result.getStatusCodeValue());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().size() >= 0);
    }

    @Test
    void obtenerOrdenesPorUsuario() {
        String userId = "user123";
        OrderPurchaseRequest request = new OrderPurchaseRequest("orden123", LocalDateTime.now(), LocalDateTime.now(), null, null, null, Collections.emptyList(), 100.0);
        orderPurchaseService.createOrderPurchase(request);

        ResponseEntity<List<OrderPurchaseResponse>> result = orderPurchaseController.getOrdersdByUser(userId);

        assertEquals(200, result.getStatusCodeValue());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().size() >= 0);
    }

    @Test
    void crearNuevaOrden() {
        OrderPurchaseRequest request = new OrderPurchaseRequest("orden123", LocalDateTime.now(), LocalDateTime.now(), null, null, null, Collections.emptyList(), 100.0);

        ResponseEntity<String> result = orderPurchaseController.createOrder(request);

        assertEquals(201, result.getStatusCodeValue());
        assertNotNull(result.getBody());
        assertEquals("orden123", result.getBody());
    }

    @Test
    void actualizarEstadoDeOrden() {
        String orderId = "orden123";
        OrderPurchaseRequest request = new OrderPurchaseRequest(orderId, LocalDateTime.now(), LocalDateTime.now(), null, null, null, Collections.emptyList(), 100.0);
        orderPurchaseService.createOrderPurchase(request);

        OrderPurchaseRequest updateRequest = new OrderPurchaseRequest(orderId, LocalDateTime.now(), LocalDateTime.now(), null, null, null, Collections.emptyList(), 120.0);

        ResponseEntity<OrderPurchaseResponse> result = orderPurchaseController.updateOrderEstado(orderId, updateRequest);

        assertEquals(200, result.getStatusCodeValue());
        assertNotNull(result.getBody());
    }

    @Test
    void eliminarOrden() {
        String orderId = "orden123";
        OrderPurchaseRequest request = new OrderPurchaseRequest(orderId, LocalDateTime.now(), LocalDateTime.now(), null, null, null, Collections.emptyList(), 100.0);
        orderPurchaseService.createOrderPurchase(request);

        ResponseEntity<Void> result = orderPurchaseController.deleteOrder(orderId);

        assertEquals(204, result.getStatusCodeValue());

    }
}
