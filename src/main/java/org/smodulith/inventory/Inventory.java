package org.smodulith.inventory;

import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.annotation.Service;

@Service
@RequiredArgsConstructor
public class Inventory {
    // injected via @RequiredArgsConstructor
    private final InventoryRepository repository;
}
