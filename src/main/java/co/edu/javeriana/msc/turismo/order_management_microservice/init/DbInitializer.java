package co.edu.javeriana.msc.turismo.order_management_microservice.init;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.Repository.OrderItemRepository;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.Repository.OrderPurchaseRepository;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.model.OrderItem;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.model.OrderPurchase;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
public class DbInitializer //implements CommandLineRunner
{

    @Autowired
    private OrderPurchaseRepository orderPurchaseRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

//    @Override
//    public void run(String... args) throws Exception {
//        Faker faker = new Faker();
//
//        // Crear OrderPurchases
//        for (int i = 0; i < 5; i++) {
//            OrderPurchase orderPurchase = new OrderPurchase();
//            orderPurchase.setCreationDate(LocalDate.now());
//            orderPurchase.setEstado(faker.options().option(co.edu.javeriana.msc.turismo.order_management_microservice.enums.Estado.class));
//            orderPurchase.setCreatedBy(faker.number().randomNumber());
//            orderPurchase.setPayId(faker.number().randomNumber());
//
//            // Crear OrderItems para cada OrderPurchase
//            for (int j = 0; j < 3; j++) {
//                OrderItem orderItem = new OrderItem();
//                orderItem.setSubtotal(faker.number().randomDouble(2, 10, 1000));
//                orderItem.setQuantity(faker.number().numberBetween(1, 10));
//                orderItem.setServiceId(faker.number().randomNumber());
//                orderItem.setOrderPurchase(orderPurchase); // Asocia el OrderItem con OrderPurchase
//
//                orderItemRepository.save(orderItem);
//            }
//
//            orderPurchaseRepository.save(orderPurchase); // Guarda el OrderPurchase
//        }
//    }
}
