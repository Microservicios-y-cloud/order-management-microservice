package co.edu.javeriana.msc.turismo.order_management_microservice.cart.controller;

import co.edu.javeriana.msc.turismo.order_management_microservice.cart.dtos.CartRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.dtos.CartResponse;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.model.Cart;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.model.CartItem;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.repository.CartRepository;
import co.edu.javeriana.msc.turismo.order_management_microservice.cart.services.CartService;
import co.edu.javeriana.msc.turismo.order_management_microservice.dto.Customer;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseRequest;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import java.time.LocalDateTime;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureGraphQlTester
@Testcontainers
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CartControllerTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

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

    static KafkaConsumer<Object, Object> mockKafkaConsumer;

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

    static final Cart carrito2 = Cart.builder()
            .id("carrito2")
            .creationDate(LocalDateTime.now())
            .lastUpdate(LocalDateTime.now())
            .createdBy(Customer.builder()
                    .id("juan2")
                    .userType("Customer")
                    .username("juand2")
                    .firstName("Juan")
                    .lastName("echeverry")
                    .email("juan_echeverry@javeriana.edu.co")
                    .build())
            .cartItems(List.of(
                    CartItem.builder()
                            .serviceId(3L)
                            .quantity(3)
                            .subtotal(300.0)
                            .build(),
                    CartItem.builder()
                            .serviceId(4L)
                            .quantity(2)
                            .subtotal(250.0)
                            .build()
            ))
            .build();

    static void createMockKafkaConsumer() {
        String groupId_1 = "service-group";
        String groupId_2 = "service-type-group";
        String groupId_3 = "my-consumer-group";
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId_1);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId_2);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId_3);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "co.edu.javeriana.msc.*");
        mockKafkaConsumer = new KafkaConsumer<>(properties, new JsonDeserializer<>(), new JsonDeserializer<>());
        mockKafkaConsumer.subscribe(List.of("cartQueue"));
    }

    @BeforeAll
    static void beforeAll(@Autowired CartRepository carritoRepository) {
        kafka.start();
        createMockKafkaConsumer();
        mongo.start();
        carritoRepository.save(carrito1);
        carritoRepository.save(carrito2);

    }

    @AfterAll
    static void afterAll() {
        mongo.stop();
        kafka.stop();
    }

    @BeforeEach
    void setup(@Autowired CartRepository carritoRepository) {
        carritoRepository.deleteAll();
        carritoRepository.save(carrito1);
        carritoRepository.save(carrito2);
    }


    @Test
    @Order(1)
    void getCartTest(@Autowired CartRepository carritoRepository) {
        CartResponse cartResponse = cartService.getCart(carrito1.getId());

        assertThat(cartResponse).isNotNull();
        assertThat(cartResponse.id()).isEqualTo(carrito1.getId());
        assertThat(cartResponse.createdBy().getUsername()).isEqualTo(carrito1.getCreatedBy().getUsername());
    }

    @Test
    @Order(2)
    void deleteCartItemTest(@Autowired CartRepository carritoRepository) {
        Long serviceIdToDelete = carrito1.getCartItems().get(0).getServiceId();

        cartService.deleteCartItem(carrito1.getId(), serviceIdToDelete);

        var updatedCart = carritoRepository.findById(carrito1.getId()).orElseThrow();
        assertThat(updatedCart.getCartItems().stream().noneMatch(item -> item.getServiceId().equals(serviceIdToDelete))).isTrue();
    }


    @Test
    @Order(3)
    void deleteCartTest(@Autowired CartRepository carritoRepository) {
        cartService.deleteCart(carrito1.getId());

        assertThat(carritoRepository.findById(carrito1.getId())).isEmpty();
    }

    @Test
    @Order(4)
    void getCartByUserTest() {
        CartResponse cartResponse = cartService.getCartByUser(carrito1.getCreatedBy().getId());

        assertThat(cartResponse).isNotNull();
        assertThat(cartResponse.createdBy().getId()).isEqualTo(carrito1.getCreatedBy().getId());
        assertThat(cartResponse.cartItems()).hasSize(carrito1.getCartItems().size());
    }

    @Test
    @Order(5)
    void purchaseTest() {
        OrderPurchaseRequest purchaseRequest = cartService.toOrderRequest(cartService.getCart(carrito1.getId()));

        assertThat(purchaseRequest).isNotNull();
        assertThat(purchaseRequest.amount()).isGreaterThan(0);
    }




}
