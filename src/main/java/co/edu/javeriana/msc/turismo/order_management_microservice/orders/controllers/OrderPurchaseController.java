package co.edu.javeriana.msc.turismo.order_management_microservice.orders.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseResponse;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.services.OrderPurchaseService;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderPurchaseController {
        
        private final OrderPurchaseService orderPurchaseService;
    
        // Obtener una orden por ID
        @GetMapping("/{order-id}")
        public ResponseEntity<OrderPurchaseResponse> getOrder(
                @PathVariable("order-id") Long orderId) {
            return ResponseEntity.ok(orderPurchaseService.findById(orderId));
        }
    
        // Obtener todas las ordenes
        @GetMapping
        public ResponseEntity<List<OrderPurchaseResponse>> findAll() {
            return ResponseEntity.ok(orderPurchaseService.findAllOrderPurchases());
        }
    
        // Crear una nueva orden
        @PostMapping
        public ResponseEntity<Long> createOrder(
                @Valid @RequestBody OrderPurchaseRequest orderRequest) {
            Long createdOrderId = orderPurchaseService.createOrderPurchase(orderRequest);
            return new ResponseEntity<>(createdOrderId, HttpStatus.CREATED);
        }
    
        // Actualizar el estado de una orden existente
        @PutMapping("/{order-id}")
        public ResponseEntity<OrderPurchaseResponse> updateOrderEstado(
                @PathVariable("order-id") Long orderId,
                @Valid @RequestBody OrderPurchaseRequest updateRequest) {
            OrderPurchaseResponse updatedOrder = orderPurchaseService.updateOrderPurchaseEstado(orderId, updateRequest);
            return ResponseEntity.ok(updatedOrder);
        }
    
        // Eliminar una orden por ID
        @DeleteMapping("/{order-id}")
        public ResponseEntity<Void> deleteOrder(@PathVariable("order-id") Long orderId) {
            orderPurchaseService.deleteOrderPurchase(orderId);
            return ResponseEntity.noContent().build();  // Devuelve 204 No Content
        }
}
