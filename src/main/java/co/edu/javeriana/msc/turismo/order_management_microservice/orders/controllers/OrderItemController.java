package co.edu.javeriana.msc.turismo.order_management_microservice.orders.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderItemRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderItemResponse;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.services.OrderItemService;

import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/ordersItems")
@RequiredArgsConstructor

public class OrderItemController {

    private final OrderItemService orderItemService;

    // Obtener un item de orden por ID
    @GetMapping("/{order-item-id}")
    public ResponseEntity<OrderItemResponse> getOrderItem(
            @PathVariable("order-item-id") Long orderItemId) {
        return ResponseEntity.ok(orderItemService.findById(orderItemId));   
    }

    // Obtener todos los items de orden
    @GetMapping
    public ResponseEntity<List<OrderItemResponse>> findAll() {
        return ResponseEntity.ok(orderItemService.findAllOrderItems());
    }

    // Crear un nuevo item de orden
    @PostMapping
    public ResponseEntity<Long> createOrderItem(
            @Valid @RequestBody OrderItemRequest orderItemRequest) {
        Long createdOrderItemId = orderItemService.createOrderItem(orderItemRequest);
        return new ResponseEntity<>(createdOrderItemId, HttpStatus.CREATED);
    }

    // Actualizar la cantidad de un item de orden existente
    @PutMapping("/{order-item-id}")
    public ResponseEntity<OrderItemResponse> updateOrderItemQuantity(
            @PathVariable("order-item-id") Long orderItemId,
            @Valid @RequestBody OrderItemRequest updateRequest) {
        OrderItemResponse updatedOrderItem = orderItemService.updateOrderItemQuantity(orderItemId, updateRequest);
        return ResponseEntity.ok(updatedOrderItem);
    }

    // Eliminar un item de orden por ID
    @DeleteMapping("/{order-item-id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable("order-item-id") Long orderItemId) {
        orderItemService.deleteOrderItem(orderItemId);
        return ResponseEntity.noContent().build();  // Devuelve 204 No Content
    }
}