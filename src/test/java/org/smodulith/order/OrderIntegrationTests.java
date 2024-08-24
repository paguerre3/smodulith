package org.smodulith.order;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.smodulith.customer.Customer;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.AssertablePublishedEvents;
import org.springframework.modulith.test.Scenario;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@ApplicationModuleTest
@RequiredArgsConstructor
public class OrderIntegrationTests {
    private final OrderManagement orderManagement;
    private final OrderRepository orderRepository;

    @Test
    void bootstrapsOrderModule() {

    }

    @Test
    void persistsAndLoadsOrder() {
        var reference = orderRepository.save(new Order(new Customer.CustomerIdentifier(UUID.randomUUID())));
        var result = orderRepository.findById(reference.getId());

        // Equal but not the same
        assertThat(result).hasValue(reference);
        assertThat(result).hasValueSatisfying(it -> assertThat(it).isNotSameAs(reference));
    }

    @Test
    void completionCausesEventPublished(AssertablePublishedEvents events) {
        var order = new Order(new Customer.CustomerIdentifier(UUID.randomUUID()));
        orderManagement.complete(order);

        assertThat(events).contains(Order.OrderCompleted.class)
                .matching(Order.OrderCompleted::oId, order.getId());
    }

    @Test
    void completionCausesEventPublished(Scenario scenario) {

        var order = new Order(new Customer.CustomerIdentifier(UUID.randomUUID()));
        scenario.stimulate(() -> orderManagement.complete(order))
                .andWaitForEventOfType(Order.OrderCompleted.class)
                .matchingMappedValue(Order.OrderCompleted::oId, order.getId())
                .toArrive();
    }
}
