package co.edu.javeriana.msc.turismo.order_management_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import me.yaman.can.webflux.h2console.H2ConsoleAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableDiscoveryClient
@Import(value={H2ConsoleAutoConfiguration.class})
public class OrderManagementMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderManagementMicroserviceApplication.class, args);
	}

}