package co.edu.javeriana.msc.turismo.order_management_microservice.queue;

import java.util.function.Consumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.UserTransactionRequest;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Configuration
public class StatusQueueConsumer {

@Bean
Consumer<Message<UserTransactionRequest>> receiveStatus() {


return message -> {
log.info("Received message: {}", message);
log.info("Payload: {}", message.getPayload());
// Se crea el evento de pago: Se hace un post del pago a la base de datos, 
//se actualiza el saldo del usuario y el estado de la orden y del pago.
};
}
}