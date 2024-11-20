package co.edu.javeriana.msc.turismo.order_management_microservice.queue;

import co.edu.javeriana.msc.turismo.order_management_microservice.cart.dtos.CartResponse;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.model.Cart;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.model.CartItem;
import co.edu.javeriana.msc.turismo.order_management_microservice.dto.Customer;
import co.edu.javeriana.msc.turismo.order_management_microservice.queue.dtos.AccommodationTypeResponse;
import co.edu.javeriana.msc.turismo.order_management_microservice.queue.repository.SuperServiceRepository;
import com.google.gson.Gson;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StatusQueueConsumerTest {

    @Autowired
    private SuperServiceRepository superServiceRepository;

    @Container
    static final MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.6");

    @Container
    static final KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.1"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", mongo::getHost);
        registry.add("spring.data.mongodb.port", mongo::getFirstMappedPort);
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    private static KafkaTemplate<String, Object> kafkaTemplate;

    static void createKafkaProducer() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
        configProps.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);
        configProps.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 5000);

        configProps.put(ProducerConfig.ACKS_CONFIG, "1");

        ProducerFactory<String, Object> producerFactory = new DefaultKafkaProducerFactory<>(configProps);
        kafkaTemplate = new KafkaTemplate<>(producerFactory);
    }

    static final Cart carrito1 = Cart.builder()
            .id("carrito1")
            .creationDate(LocalDateTime.now())
            .lastUpdate(LocalDateTime.now())
            .createdBy(Customer.builder()
                    .id("juan")
                    .userType("Customer")
                    .username("juand")
                    .firstName("juan")
                    .lastName("echeverry")
                    .email("juan_echeverry@javeriana.edu.co")
                    .build())
            .cartItems(List.of(
                    CartItem.builder()
                            .serviceId(1L)
                            .quantity(2)
                            .subtotal(200.0)
                            .build(),
                    CartItem.builder()
                            .serviceId(2L)
                            .quantity(1)
                            .subtotal(150.0)
                            .build()
            ))
            .build();

    static AccommodationTypeResponse AccType = new AccommodationTypeResponse(90L, "Lugar");

    @BeforeAll
    static void setup() {
        mongo.start();
        kafka.start();

        createKafkaProducer();

        Gson gson = new Gson();
        kafkaTemplate.send("order-payment-queue", gson.toJson(AccType));
    }

    @AfterAll
    static void teardown() {
        kafka.stop();
        mongo.stop();
    }

    @Test
    @Order(1)
    void receiveLocation() {
        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            assertThat(superServiceRepository.findAll()).isNotNull();
        });
    }
}