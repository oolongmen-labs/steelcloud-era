package com.steelcloud.core.api.currency;

import net.minecraft.item.Item;

public record Denomination(int value, Item item) {
    public Denomination {
        if (value <= 0) throw new IllegalArgumentException("value must be positive");
        if (item == null) throw new IllegalArgumentException("item must not be null");
    }
}