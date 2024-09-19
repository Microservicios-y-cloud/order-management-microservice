package co.edu.javeriana.msc.turismo.order_management_microservice.orders.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.javeriana.msc.turismo.order_management_microservice.orders.model.OrderItem;


@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long>  {
}
