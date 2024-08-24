package org.smodulith.order;

import jakarta.persistence.Table;
import lombok.Getter;
import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Association;
import org.jmolecules.ddd.types.Entity;
import org.jmolecules.ddd.types.Identifier;
import org.jmolecules.event.annotation.Externalized;
import org.jmolecules.event.types.DomainEvent;
import org.smodulith.customer.Customer;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Table(name = "MyOrder")
public class Order extends AbstractAggregateRoot<Order> implements AggregateRoot<Order, Order.OrderIdentifier> {
    private final OrderIdentifier id;
    private final Association<Customer, Customer.CustomerIdentifier> customer;
    private Status status;
    private final List<LineItem> lineItems = new ArrayList<>();

    public Order(Customer.CustomerIdentifier customerId) {
        this.id = new OrderIdentifier(UUID.randomUUID());
        this.customer = Association.forId(customerId);
        this.status = Status.OPEN;
    }


    Order complete() {
        this.status = Status.COMPLETED;
        this.registerEvent(new OrderCompleted(this.id));
        return this;
    }

    Order add(LineItem item) {
        this.lineItems.add(item);
        return this;
    }

    public record OrderIdentifier(UUID id) implements Identifier {
    }

    @Externalized("orders.OrderCompleted")
    public record OrderCompleted(OrderIdentifier oId) implements DomainEvent{}

    enum Status {
        OPEN, COMPLETED, CANCELLED
    }

    @Getter
    class LineItem implements Entity<Order, LineItem.LineItemId> {
        private final LineItemId id;
        private final String description;
        private final long amount;

        LineItem(String description, long amount) {
            this.id = new LineItemId(UUID.randomUUID());
            this.description = description;
            this.amount = amount;
        }

        record LineItemId (UUID id) implements Identifier{}
    }
}
