package co.edu.javeriana.msc.turismo.order_management_microservice.queue.dtos;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MyMessageDTO implements Serializable {
    private LocalDateTime dateTime;
    private String myMessageText;
}
