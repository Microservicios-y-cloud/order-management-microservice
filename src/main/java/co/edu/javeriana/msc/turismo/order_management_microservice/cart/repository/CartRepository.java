package co.edu.javeriana.msc.turismo.order_management_microservice.cart.repository;

import co.edu.javeriana.msc.turismo.order_management_microservice.cart.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByCreatedBy(String createdBy);
}
