package co.edu.javeriana.msc.turismo.order_management_microservice.orders.services;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.Repository.OrderItemRepository;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderItemRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderItemResponse;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.mappers.OrderItemMapper;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class OrderItemService {

    private final OrderItemRepository repository;

    public OrderItemService(OrderItemRepository repository) {
        this.repository = repository;
    }

    public Long createOrderItem(OrderItemRequest request) {
        var orderItem = OrderItemMapper.toOrderItem(request);
        return repository.save(orderItem).getId();
    }

    public OrderItemResponse findById(Long orderItemId) {
        return repository.findById(orderItemId)
                .map(OrderItemMapper::toOrderItemResponse)
                .orElseThrow(() -> new EntityNotFoundException("OrderItem not found, with id: " + orderItemId));
    }

    public List<OrderItemResponse> findAllOrderItems() {
        return repository.findAll()
                .stream()
                .map(OrderItemMapper::toOrderItemResponse)
                .collect(Collectors.toList());
    }

    public OrderItemResponse updateOrderItemQuantity(Long orderItemId, OrderItemRequest orderItemRequest) {
        var orderItem = repository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("OrderItem not found with id: " + orderItemId));

        orderItem.setQuantity(orderItemRequest.quantity());

        var updatedOrderItem = repository.save(orderItem);

        return OrderItemMapper.toOrderItemResponse(updatedOrderItem);
    }

    public void deleteOrderItem(Long orderItemId) {
        repository.deleteById(orderItemId);
    }
}