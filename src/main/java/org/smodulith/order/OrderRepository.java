package org.smodulith.order;

import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Order.OrderIdentifier> {
}
