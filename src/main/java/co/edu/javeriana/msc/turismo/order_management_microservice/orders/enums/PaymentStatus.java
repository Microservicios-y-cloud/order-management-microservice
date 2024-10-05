package co.edu.javeriana.msc.turismo.order_management_microservice.orders.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    ACEPTADA(1),
    RECHAZADA(2);


    private final int codigo;

    PaymentStatus(int codigo) {
        this.codigo = codigo;
    }

}