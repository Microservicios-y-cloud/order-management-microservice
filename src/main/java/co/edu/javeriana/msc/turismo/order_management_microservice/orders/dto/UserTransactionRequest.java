package co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto;

import java.io.Serializable;

import co.edu.javeriana.msc.turismo.order_management_microservice.dto.Customer;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.enums.PaymentStatus;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTransactionRequest implements Serializable{
    private String orderId;
    private Customer user;
    private Double amount;
    Status status;
    PaymentStatus paymentStatus;
}
