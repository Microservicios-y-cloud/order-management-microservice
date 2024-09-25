package co.edu.javeriana.msc.turismo.order_management_microservice.orders.Repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.model.OrderPurchase;


public interface OrderPurchaseRepository extends MongoRepository<OrderPurchase, String> {
    Optional<OrderPurchase> findByCreatedBy(String createdBy);
}
