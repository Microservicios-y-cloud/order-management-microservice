package co.edu.javeriana.msc.turismo.order_management_microservice.orders.event;

import java.util.Date;
import java.util.UUID;

public interface Event {

    UUID getEventId();

    Date getDate();
}