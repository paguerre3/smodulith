package org.smodulith;

import org.smodulith.customer.Customer;
import org.smodulith.order.Order;
import org.smodulith.order.OrderManagement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.UUID;

/**
 * The `@TestConfiguration` annotation is a specialized
 * form of `@Configuration` that's useful for writing unit tests
 * in Spring Boot applications. Here's what it does:
 * 1. **Custom Beans**: You can use `@TestConfiguration`
 * to define additional beans specific to tests.
 * These beans can be used to override existing beans or
 * add specialized configurations for testing purposes.
 * 2. **No Auto-Detection Block**:
 * Unlike regular `@Configuration` classes,
 * the use of `@TestConfiguration` doesn't prevent
 * auto-detection of `@SpringBootConfiguration`.
 * In summary, it allows you to modify Spring's application
 * context during test runtime,
 * making it easier to create custom configurations
 * for testing components in your Spring Boot application.
 *
 */
@TestConfiguration
public class TestSmodulithApplication {

    @Bean
    @ServiceConnection
    KafkaContainer kafkaContainer() {
        System.out.println("###KafkaContainer image run...");
        return new KafkaContainer(DockerImageName.parse("apache/kafka-native:latest"));
    }

    public static void main(String[] args) {
        var ctx = SpringApplication.from(SmodulithApplication::main)
                .with(TestSmodulithApplication.class)
                .run(args)
                .getApplicationContext();

        var orders = ctx.getBean(OrderManagement.class);
        var completed = orders.complete(new Order(new Customer.CustomerIdentifier(UUID.randomUUID())));
        System.out.printf("###Order Complete from Kafka Container Done### %s%n", completed);
    }
}
