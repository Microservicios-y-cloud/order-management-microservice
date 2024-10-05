package co.edu.javeriana.msc.turismo.order_management_microservice.cart.model;

import co.edu.javeriana.msc.turismo.order_management_microservice.dto.Customer;
import jakarta.persistence.Id;
import jakarta.persistence.UniqueConstraint;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
    @Indexed(unique = true)
    private Customer createdBy;

    private List<CartItem> cartItems;
}
