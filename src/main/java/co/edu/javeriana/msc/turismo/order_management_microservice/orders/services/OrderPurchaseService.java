package co.edu.javeriana.msc.turismo.order_management_microservice.orders.services;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.mappers.OrderPurchaseMapper;
import co.edu.javeriana.msc.turismo.order_management_microservice.queue.repository.SuperServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.Repository.OrderPurchaseRepository;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderPurchaseService {

    private final OrderPurchaseRepository repository;
    private final SuperServiceRepository superServiceRepository;

    public String createOrderPurchase(@Valid OrderPurchaseRequest request) {
        for (var orderItem : request.orderItems()) {
            if (!superServiceRepository.existsById(orderItem.getServiceId())) {
                throw new RuntimeException("Service not found");
            }
        }
        var orderPurchase = OrderPurchaseMapper.toOrderPurchase(request);
        return repository.save(orderPurchase).getIdOrderPurchase();
    }

    public OrderPurchaseResponse findById(String orderPurchaseId) {
        return repository.findById(orderPurchaseId)
                .map(OrderPurchaseMapper::toOrderPurchaseResponse)
                .orElseThrow(() -> new EntityNotFoundException("OrderPurchase not found, with id: " + orderPurchaseId));
    }

    public List<OrderPurchaseResponse> findAllOrderPurchases() {
        return repository.findAll()
                .stream()
                .map(OrderPurchaseMapper::toOrderPurchaseResponse)
                .collect(Collectors.toList());
    }

    public OrderPurchaseResponse updateOrderPurchaseEstado(String orderPurchaseId, OrderPurchaseRequest orderPurchaseRequest) {
        var orderPurchase = repository.findById(orderPurchaseId)
                .orElseThrow(() -> new EntityNotFoundException("OrderPurchase not found with id: " + orderPurchaseId));

        for (var orderItem : orderPurchaseRequest.orderItems()) {
            if (!superServiceRepository.existsById(orderItem.getServiceId())) {
                throw new RuntimeException("Service not found");
            }
        }
        // Actualizar solo el estado de la compra
        orderPurchase.setOrderStatus(orderPurchaseRequest.orderStatus());

        // Usar merge para asegurarnos de que la entidad esté gestionada
        var updatedOrderPurchase = repository.save(orderPurchase);

        // Devolver la respuesta actualizada
        return OrderPurchaseMapper.toOrderPurchaseResponse(updatedOrderPurchase);
    }

    public void deleteOrderPurchase(String orderPurchaseId) {
        repository.deleteById(orderPurchaseId);
    }
}