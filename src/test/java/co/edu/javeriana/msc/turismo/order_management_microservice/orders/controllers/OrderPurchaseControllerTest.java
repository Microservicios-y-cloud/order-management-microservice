package co.edu.javeriana.msc.turismo.order_management_microservice.orders.controllers;

import co.edu.javeriana.msc.turismo.order_management_microservice.cart.repository.CartRepository;
import co.edu.javeriana.msc.turismo.order_management_microservice.dto.Customer;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.Repository.OrderPurchaseRepository;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseRequest;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.dto.OrderPurchaseResponse;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.enums.PaymentStatus;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.enums.Status;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.model.OrderItem;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.model.OrderPurchase;
import co.edu.javeriana.msc.turismo.order_management_microservice.orders.services.OrderPurchaseService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureGraphQlTester
@Testcontainers
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OrderPurchaseControllerTest {

    @Autowired
    private OrderPurchaseService orderPurchaseService;

    @Autowired
    private OrderPurchaseRepository orderPurchaseRepository;

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

    static final OrderPurchase order1 = OrderPurchase.builder()
            .id("1808")
            .creationDate(LocalDateTime.now())
            .lastUpdate(LocalDateTime.now())
            .orderStatus(Status.ACEPTADA)
            .paymentStatus(PaymentStatus.ACEPTADA)
            .createdBy(Customer.builder()
                    .id("1808")
                    .userType("Customer")
                    .username("juan")
                    .firstName("juan")
                    .lastName("juan")
                    .email("juanjuan@juan.com")
                    .build())
            .orderItems(List.of(
                    OrderItem.builder()
                            .serviceId(1L)
                            .quantity(2)
                            .subtotal(100.0)
                            .build(),
                    OrderItem.builder()
                            .serviceId(2L)
                            .quantity(1)
                            .subtotal(75.0)
                            .build()
            ))
            .amount(175.0)
            .build();

    static final OrderPurchase order2 = OrderPurchase.builder()
            .id("1809")
            .creationDate(LocalDateTime.now())
            .lastUpdate(LocalDateTime.now())
            .orderStatus(Status.RECHAZADA)
            .paymentStatus(PaymentStatus.RECHAZADA)
            .createdBy(Customer.builder()
                    .id("1809")
                    .userType("Customer")
                    .username("juand")
                    .firstName("juan")
                    .lastName("juan")
                    .email("juanjuan@juan.com")
                    .build())
            .orderItems(List.of(
                    OrderItem.builder()
                            .serviceId(3L)
                            .quantity(3)
                            .subtotal(180.0)
                            .build(),
                    OrderItem.builder()
                            .serviceId(4L)
                            .quantity(2)
                            .subtotal(80.0)
                            .build()
            ))
            .amount(260.0)
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
    static void beforeAll(@Autowired OrderPurchaseRepository orderRepository) {
        kafka.start();
        createMockKafkaConsumer();
        mongo.start();
        orderRepository.save(order1);
        orderRepository.save(order2);
    }

    @AfterAll
    static void afterAll() {
        mongo.stop();
        kafka.stop();
    }

    @BeforeEach
    void setup(@Autowired OrderPurchaseRepository orderRepository) {
        //orderRepository.deleteAll();
        orderRepository.save(order1);
        orderRepository.save(order2);
    }

    @AfterEach
    void after(@Autowired OrderPurchaseRepository orderRepository) {
        orderRepository.delete(order1);
        orderRepository.delete(order2);
    }

    @Test
    @Order(1)
    void getOrderByIdTest(@Autowired OrderPurchaseRepository orderRepository) {
        OrderPurchaseResponse orderResponse = orderPurchaseService.findById(order1.getId());

        assertThat(orderResponse).isNotNull();
        assertThat(orderResponse.id()).isEqualTo(order1.getId());
        assertThat(orderResponse.createdBy().getUsername()).isEqualTo(order1.getCreatedBy().getUsername());
    }

    @Test
    @Order(2)
    void getAllOrdersTest() {
        List<OrderPurchaseResponse> allOrders = orderPurchaseService.findAllOrderPurchases();

        assertThat(allOrders).isNotNull();
    }

    @Test
    @Order(3)
    void getPurchasedByUserTest() {
        List<OrderPurchaseResponse> purchasedOrders = orderPurchaseService.findAllPurchasedByUsers(order1.getCreatedBy().getId());

        assertThat(purchasedOrders).isNotNull();
        assertThat(purchasedOrders).hasSize(1);
        assertThat(purchasedOrders.get(0).id()).isEqualTo(order1.getId());
    }

    @Test
    @Order(4)
    void getOrderedByUserTest() {
        List<OrderPurchaseResponse> orderedOrders = orderPurchaseService.findAllOrdersByUsers(order2.getCreatedBy().getId());

        assertThat(orderedOrders).isNotNull();
        assertThat(orderedOrders).hasSize(1);
        assertThat(orderedOrders.get(0).id()).isEqualTo(order2.getId());
    }


    @Test
    @Order(5)
    void deleteOrderTest() {
        orderPurchaseService.deleteOrderPurchase(order1.getId());

        assertThat(orderPurchaseRepository.findById(order1.getId())).isEmpty();
    }



}
