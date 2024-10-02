package co.edu.javeriana.msc.turismo.order_management_microservice.queue.repository;


import co.edu.javeriana.msc.turismo.order_management_microservice.queue.dtos.SuperService;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SuperServiceRepository extends MongoRepository<SuperService, Long> {
}
