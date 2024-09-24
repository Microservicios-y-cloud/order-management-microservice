package co.edu.javeriana.msc.turismo.order_management_microservice.cart.model;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Cart {
    @Id
    private String id;
    @CreatedDate
    private LocalDateTime creationDate;

    private LocalDateTime lastUpdate;
    private String createdBy;

    private List<CartItem> cartItems;
}
