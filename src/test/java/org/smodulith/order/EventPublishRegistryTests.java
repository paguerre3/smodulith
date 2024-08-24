package org.smodulith.order;

import static org.assertj.core.api.Assertions.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.smodulith.TestSmodulithApplication;
import org.smodulith.customer.Customer;
import org.springframework.context.annotation.Import;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.modulith.events.EventExternalized;
import org.springframework.modulith.events.core.EventPublicationRegistry;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

/**
 * The @Import annotation in Spring is a powerful mechanism for importing additional Java-based configuration classes
 * into the application context.
 * NO NEED to set import of TestSmodulithApplication.class that
 * has kafkaContainer if running docker-compose of kafka.yml.
 * The @DirtiesContext is used to reset the Spring context if needed.
 */
@ApplicationModuleTest
@RequiredArgsConstructor
@Import({ EventPublishRegistryTests.FailingAsyncTransactionalEventListener.class /*, TestSmodulithApplication.class*/ })
@DirtiesContext
@TestPropertySource(properties = "spring.modulith.events.externalization.enabled=true")
public class EventPublishRegistryTests {
    private final OrderManagement orderManagement;
    private final EventPublicationRegistry eventPublicationRegistry;
    private final FailingAsyncTransactionalEventListener failingAsyncTransactionalEventListener;

    /**
     * In the context of Spring Modulith,
     * the Scenario class provides a declarative
     * DSL (Domain-Specific Language) for defining
     * integration testing scenarios for application modules.
     * Scenario API:
     * The Scenario class offers a fluent API for defining
     * integration testing scenarios.
     * It starts with a "stimulus"
     * (e.g., a component invocation or event publication)
     * and defines the expected outcome.
     * You can use stimulate(Function) or publish(Object...)
     * to set up the scenario.
     *
     * @param scenario Scenario
     */
    @Test
    void leavesPublicationIncompleteForFailingListener(Scenario scenario) throws Exception{
        var order = new Order(new Customer.CustomerIdentifier(UUID.randomUUID()));

        scenario.stimulate(() -> orderManagement.complete(order))
                .andWaitForEventOfType(EventExternalized.class)
                .toArriveAndVerify(__ -> {
                    assertThat(failingAsyncTransactionalEventListener.getEx()).isNotNull();
                    assertThat(eventPublicationRegistry.findIncompletePublications()).hasSize(1);
                });
    }

    @Getter
    static class FailingAsyncTransactionalEventListener {
        Exception ex;

        @ApplicationModuleListener
        void on(Order.OrderCompleted event) throws Exception {
            var exception = new IllegalStateException("Error while Publishing Event");
            try {
                throw exception;
            } finally {
                this.ex = exception;
            }
        }
    }
}
