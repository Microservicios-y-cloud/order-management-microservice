package co.edu.javeriana.msc.turismo.order_management_microservice.orders.services;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.mappers.OrderPurchaseMapper;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.Repository.OrderPurchaseRepository;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderPurchaseService {

    private final OrderPurchaseRepository repository;

    // Constructor con inyección de dependencias
    public OrderPurchaseService(OrderPurchaseRepository repository) {
        this.repository = repository;
    }

    public String createOrderPurchase(@Valid OrderPurchaseRequest request) {
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

        // Actualizar solo el estado de la compra
        orderPurchase.setEstado(orderPurchaseRequest.estado());

        // Usar merge para asegurarnos de que la entidad esté gestionada
        var updatedOrderPurchase = repository.save(orderPurchase);

        // Devolver la respuesta actualizada
        return OrderPurchaseMapper.toOrderPurchaseResponse(updatedOrderPurchase);
    }

    public void deleteOrderPurchase(String orderPurchaseId) {
        repository.deleteById(orderPurchaseId);
    }
}