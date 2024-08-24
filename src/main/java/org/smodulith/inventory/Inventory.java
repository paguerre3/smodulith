package org.smodulith.inventory;

import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.annotation.Service;
import org.smodulith.order.Order;
import org.springframework.modulith.events.ApplicationModuleListener;

@Service
@RequiredArgsConstructor
public class Inventory {
    // injected via @RequiredArgsConstructor
    private final InventoryRepository repository;

    @ApplicationModuleListener
    void updateStock(Order.OrderCompleted order) {
        // Updates the stock for all line items contained in the order.
    }
}
