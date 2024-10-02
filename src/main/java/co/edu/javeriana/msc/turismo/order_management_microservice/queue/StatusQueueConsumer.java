package co.edu.javeriana.msc.turismo.order_management_microservice.queue;

import java.util.function.Consumer;

import co.edu.javeriana.msc.turismo.order_management_microservice.queue.dtos.SuperServiceDTO;
import co.edu.javeriana.msc.turismo.order_management_microservice.queue.repository.SuperServiceRepository;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.Repository.OrderPurchaseRepository;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.UserTransactionRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.mappers.OrderPurchaseMapper;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.services.OrderPurchaseService;
import lombok.extern.slf4j.Slf4j;

import static co.edu.javeriana.msc.turismo.order_management_microservice.enums.CRUDEventType.*;


@Slf4j
@Configuration
@AllArgsConstructor
public class StatusQueueConsumer {

    private final SuperServiceRepository repository;
    private final OrderPurchaseService orderPurchaseService;
    private final OrderPurchaseRepository orderPurchaseRepository;
    private final OrderPurchaseMapper orderPurchaseMapper;


    @Bean
    Consumer<Message<UserTransactionRequest>> receiveStatus() {
        return message -> {
            log.info("Received message: {}", message);
            log.info("Payload: {}", message.getPayload());
            // Se crea el evento de pago: Se hace un post del pago a la base de datos, 
            //se actualiza el saldo del usuario y el estado de la orden y del pago.
            UserTransactionRequest userTransactionRequest = message.getPayload();            
            String orderPurchaseid = userTransactionRequest.getOrderId();

            var orderPurchase = orderPurchaseRepository.findById(orderPurchaseid);
            orderPurchase.orElseThrow(() -> new RuntimeException("Order not found"));
            orderPurchase.get().setOrderStatus(userTransactionRequest.getStatus());
            orderPurchase.get().setPaymentStatus(userTransactionRequest.getPaymentStatus());
            OrderPurchaseRequest orderPurchaseRequest = OrderPurchaseMapper.toOrderPurchaseRequest(orderPurchase.get());
            log.info("OrderPurchaseRequest: {}", orderPurchase.get());
            orderPurchaseService.updateOrderPurchaseEstado(orderPurchaseid, orderPurchaseRequest);
        };
    }

    @Bean
    Consumer<Message<SuperServiceDTO>> receiveMessage() {
        return message -> {
            log.info("Received message: {}", message);
            log.info("Payload: {}", message.getPayload());
            //hacemos un switch case en caso de que sea CREATE, UPDATE, O DELETE
            switch (message.getPayload().getEventType()) {
                case CREATE:
                    repository.save(message.getPayload().getSuperService());
                    log.info("Service saved: {}", message.getPayload().getSuperService());
                    break;
//                case UPDATE:
//                    repository.save(new SuperService(message.getPayload().getService()));
//                    break;
//                case DELETE:
//                    repository.deleteById(message.getPayload().getService().getId());
//                    break;
                default:
                    log.error("Invalid action: {}", message.getPayload().getEventType());
                    break;
            }
        };
    }
}