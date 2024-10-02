package co.edu.javeriana.msc.turismo.order_management_microservice.queue.dtos;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "accommodationTypes")
public record AccommodationTypeResponse(
        @Id
        Long accomodationTypeId,
        String name
) {
}