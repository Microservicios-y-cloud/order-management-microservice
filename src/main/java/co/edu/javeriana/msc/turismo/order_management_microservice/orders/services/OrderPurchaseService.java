package co.edu.javeriana.msc.turismo.order_management_microservice.orders.services;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.mappers.OrderPurchaseMapper;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.model.OrderPurchase;
import co.edu.javeriana.msc.turismo.order_management_microservice.queue.repository.SuperServiceRepository;
import co.edu.javeriana.msc.turismo.order_management_microservice.queue.services.MessageQueueService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.Repository.OrderPurchaseRepository;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseResponse;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.UserTransactionRequest;
import java.util.UUID;

import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.ThreadLocalRandom;



@Service
@AllArgsConstructor
@Slf4j
public class OrderPurchaseService {

    private final OrderPurchaseRepository repository;
    private final SuperServiceRepository superServiceRepository;
    private final MessageQueueService messageQueueService;

    public String createOrderPurchase(@Valid OrderPurchaseRequest request) {
        for (var orderItem : request.orderItems()) {
            if (!superServiceRepository.existsById(orderItem.getServiceId())) {
                throw new RuntimeException("Service not found");
            }
        }
        
        var orderPurchase = OrderPurchaseMapper.toOrderPurchase(request);
        
        var auxOrderPurchase = repository.save(orderPurchase);
        log.info("PRUEBAAAAA: {}", auxOrderPurchase.getId());

        messageQueueService.sendPaymentOrder(new UserTransactionRequest(auxOrderPurchase.getId(), auxOrderPurchase.getCreatedBy().getId(),
                                                                    auxOrderPurchase.getAmount(), auxOrderPurchase.getOrderStatus(),
                                                                    auxOrderPurchase.getPaymentStatus()));
        return auxOrderPurchase.getId();
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
        // Buscar la orden por su ID
        var orderPurchase = repository.findById(orderPurchaseId)
                .orElseThrow(() -> new EntityNotFoundException("OrderPurchase not found with id: " + orderPurchaseId));
    
        // Validar que los servicios asociados a los items de la orden existen
        for (var orderItem : orderPurchaseRequest.orderItems()) {
            if (!superServiceRepository.existsById(orderItem.getServiceId())) {
                throw new RuntimeException("Service not found with id: " + orderItem.getServiceId());
            }
        }
    
        // Actualizar el estado de la compra y del pago
        orderPurchase.setOrderStatus(orderPurchaseRequest.orderStatus());
        orderPurchase.setPaymentStatus(orderPurchaseRequest.paymentStatus());
    
        // Si quieres asegurarte de que la entidad está gestionada, puedes evitar la duplicación usando el ID correctamente
        // El repositorio debe actualizar la entidad si el ID ya está en la base de datos
        var updatedOrderPurchase = repository.save(orderPurchase);
    
        // Devolver la respuesta con la entidad actualizada
        return OrderPurchaseMapper.toOrderPurchaseResponse(updatedOrderPurchase);
    }

    public void deleteOrderPurchase(String orderPurchaseId) {
        repository.deleteById(orderPurchaseId);
    }
}