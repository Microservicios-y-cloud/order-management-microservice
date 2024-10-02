package co.edu.javeriana.msc.turismo.service_rating_microservice.queue.repository;

import co.edu.javeriana.msc.turismo.service_rating_microservice.queue.dtos.SuperService;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SuperServiceRepository extends MongoRepository<SuperService, Long> {
}
