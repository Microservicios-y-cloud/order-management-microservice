package co.edu.javeriana.msc.turismo.order_management_microservice.init;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.Repository.OrderItemRepository;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.Repository.OrderPurchaseRepository;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.model.OrderItem;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.model.OrderPurchase;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import co.edu.javeriana.msc.turismo.order_management_microservice.enums.Estado;

import java.time.LocalDate;
@Component
public class DbInitializer implements CommandLineRunner {

    @Autowired
    private OrderPurchaseRepository orderPurchaseRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        for (int i = 0; i < 10; i++) {
            OrderPurchase orderPurchase = OrderPurchase.builder()
                    .creationDate(LocalDate.now())
                    .estado(faker.options().option(Estado.class))
                    .createdBy(faker.number().randomNumber())
                    .build();
            orderPurchaseRepository.save(orderPurchase);
            for (int j = 0; j < 5; j++) {
                OrderItem orderItem = OrderItem.builder()
                        .subtotal(faker.number().randomDouble(2, 100, 1000))
                        .quantity(faker.number().randomDigitNotZero())
                        .serviceId(faker.number().randomNumber())
                        .orderPurchase(orderPurchase)
                        .build();
                orderItemRepository.save(orderItem);
            }
        }
    }
}
