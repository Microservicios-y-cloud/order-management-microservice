package co.edu.javeriana.msc.turismo.order_management_microservice.orders.services;

import co.edu.javeriana.msc.turismo.order_management_microservice.cart.services.CartService;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.mappers.OrderPurchaseMapper;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.mappers.PurchaseNotificationMapper;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.mappers.PurchasedInformationMapper;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.model.OrderPurchase;
import co.edu.javeriana.msc.turismo.order_management_microservice.queue.dtos.PurchaseNotification;
import co.edu.javeriana.msc.turismo.order_management_microservice.queue.dtos.PurchasedInformation;
import co.edu.javeriana.msc.turismo.order_management_microservice.queue.repository.SuperServiceRepository;
import co.edu.javeriana.msc.turismo.order_management_microservice.queue.services.MessageQueueService;
import jakarta.ws.rs.ServiceUnavailableException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.Repository.OrderPurchaseRepository;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseResponse;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.UserTransactionRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.enums.PaymentStatus;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.enums.Status;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final PurchaseNotificationMapper purchaseNotificationMapper;
    private final PurchasedInformationMapper purchasedInformationMapper;
    private final CartService cartService;

    public String createOrderPurchase(@Valid OrderPurchaseRequest request) {
        for (var orderItem : request.orderItems()) {
            if (!superServiceRepository.existsById(orderItem.getServiceId())) {
                throw new EntityNotFoundException("Service not found");
            }
        }

        //Reglas de negocio
        //Comprobar que la orden tenga items
        if(request.orderItems().isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item. Please try again.");
        }
        //Comprobar que la fecha de creación de la orden sea válida
        if(request.creationDate().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Creation date must be valid");
        }
        
        var orderPurchase = OrderPurchaseMapper.toOrderPurchase(request);
        
        var auxOrderPurchase = repository.save(orderPurchase);
        log.info("PRUEBAAAAA: {}", auxOrderPurchase.getId());

        var sent = messageQueueService.sendPaymentOrder(new UserTransactionRequest(auxOrderPurchase.getId(), auxOrderPurchase.getCreatedBy(),
                                                                    auxOrderPurchase.getAmount(), auxOrderPurchase.getOrderStatus(),
                                                                    auxOrderPurchase.getPaymentStatus()));
        if(!sent) {
            throw new ServiceUnavailableException("Error sending user transaction to queue. Kafka service is currently unavailable. Please try again later.");
        }
        return auxOrderPurchase.getId();
    }

    public OrderPurchaseResponse findById(String orderPurchaseId) {
        return repository.findById(orderPurchaseId)
                .map(OrderPurchaseMapper::toOrderPurchaseResponse)
                .orElseThrow(() -> new EntityNotFoundException("OrderPurchase not found, with id: " + orderPurchaseId));
    }

    public List<OrderPurchaseResponse> findAllOrdersByUsers(String id) {
        List<OrderPurchaseResponse> all = repository.findAll()
            .stream()
            .map(OrderPurchaseMapper::toOrderPurchaseResponse)
            .collect(Collectors.toList());
    
        List<OrderPurchaseResponse> ordersByUserApproved = new ArrayList<>();
    
        for (OrderPurchaseResponse order : all) {
            System.out.println(order.id());
            
            if (order.createdBy().getId().equals(id)) {
                ordersByUserApproved.add(order);
            }
        }
        return ordersByUserApproved;
    }

    public List<OrderPurchaseResponse> findAllPurchasedByUsers(String id) {
    List<OrderPurchaseResponse> all = repository.findAll()
        .stream()
        .map(OrderPurchaseMapper::toOrderPurchaseResponse)
        .collect(Collectors.toList());

    List<OrderPurchaseResponse> ordersByUserApproved = new ArrayList<>();

    for (OrderPurchaseResponse order : all) {
        System.out.println(order.id());
        
        //Devuelve unicamente las ordenes que se pudieron pagar
        if (order.createdBy().getId().equals(id) && order.orderstatus() == Status.ACEPTADA && order.paymentStatus() == PaymentStatus.ACEPTADA) {
            ordersByUserApproved.add(order);
        }
    }
    return ordersByUserApproved;
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
                throw new EntityNotFoundException("Service not found with id: " + orderItem.getServiceId());
            }
        }
    
        // Actualizar el estado de la compra y del pago
        orderPurchase.setOrderStatus(orderPurchaseRequest.orderStatus());
        orderPurchase.setPaymentStatus(orderPurchaseRequest.paymentStatus());
    
        // Si quieres asegurarte de que la entidad está gestionada, puedes evitar la duplicación usando el ID correctamente
        // El repositorio debe actualizar la entidad si el ID ya está en la base de datos
        var updatedOrderPurchase = repository.save(orderPurchase);

        var sent = messageQueueService.sendOrderNotification(purchaseNotificationMapper.toPurchaseNotification(updatedOrderPurchase));
        if (!sent) {
            throw new ServiceUnavailableException("Error sending purchase notification to queue. Kafka service is currently unavailable. Please try again later.");
        }
        log.info("MESSAGE SENT: {}", updatedOrderPurchase);

        if(orderPurchaseRequest.paymentStatus() == PaymentStatus.ACEPTADA){
            var sent2 = messageQueueService.sendOrderQualification(purchasedInformationMapper.toPurchasedInformation(updatedOrderPurchase));
            if (!sent2) {
                throw new ServiceUnavailableException("Error sending purchase qualification to queue. Kafka service is currently unavailable. Please try again later.");
            }
            log.info("MESSAGE SENT TO RATINGS AAA: {}", purchasedInformationMapper.toPurchasedInformation(updatedOrderPurchase).toString());
            //se revisa si hay un carrito con los items asociados a el usuario y si son los mismos que están en orderPurchase
            var cart = cartService.getCartByUser(orderPurchaseRequest.createdBy().getId());
            if (cart != null) {
                    cartService.deleteCart(cart.id());
            }
        }
    
        // Devolver la respuesta con la entidad actualizada
        return OrderPurchaseMapper.toOrderPurchaseResponse(updatedOrderPurchase);
    }

    public void deleteOrderPurchase(String orderPurchaseId) {
        repository.deleteById(orderPurchaseId);
    }
}