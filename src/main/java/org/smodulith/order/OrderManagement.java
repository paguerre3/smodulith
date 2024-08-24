package org.smodulith.order;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.annotation.Service;
import org.smodulith.customer.Customer;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderManagement {
    // injected in constructor (IoC) via Lombok
    private final OrderRepository orderRepository;

    public Order create(Customer.CustomerIdentifier customerId) {
        return new Order(customerId);
    }

    public Order complete(Order order) {
        return orderRepository.save(order.complete());
    }
}
