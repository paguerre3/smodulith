package org.smodulith.customer;

import lombok.Getter;
import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Identifier;

import java.util.UUID;

@Getter
public class Customer implements AggregateRoot<Customer, Customer.CustomerIdentifier> {
    private final CustomerIdentifier id;
    private final String address;

    /**
     * Not null API verification is enforced via package-info annotation.
     *
     * @param address String
     */
    public Customer(String address) {
        this.id = new CustomerIdentifier(UUID.randomUUID());
        this.address = address;
    }

    public record CustomerIdentifier(UUID id) implements Identifier {}
}
