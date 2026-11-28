package com.steelcloud.core.api.currency;

import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record CurrencySpec(
        Identifier id,
        Text displayName,
        String symbol,
        List<Denomination> denominations) {

    public CurrencySpec {
        if (id == null) throw new IllegalArgumentException("id must not be null");
        if (displayName == null) throw new IllegalArgumentException("displayName must not be null");
        if (denominations == null || denominations.isEmpty()) {
            throw new IllegalArgumentException("denominations must not be empty");
        }
        // 降序排序
        List<Denomination> sorted = denominations.stream()
                .sorted(Comparator.comparingInt(Denomination::value).reversed())
                .toList();
        // 校验面额值唯一
        Set<Integer> seen = new HashSet<>();
        // 同一 Item 只能对应一个面额，否则物品栏扫描与找零无法确定面额
        Set<Item> seenItems = new HashSet<>();
        for (Denomination d : sorted) {
            if (!seen.add(d.value())) {
                throw new IllegalArgumentException("Duplicate denomination value: " + d.value());
            }
            if (!seenItems.add(d.item())) {
                throw new IllegalArgumentException(
                        "Item used by more than one denomination: " + d.item());
            }
        }
        denominations = sorted;
    }
}