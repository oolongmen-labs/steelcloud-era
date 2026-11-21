package com.steelcloud.core.api.slot;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Predicate;

public record SlotDefinition(
        Identifier id,
        Predicate<ItemStack> validator,
        boolean visible,
        int order,
        Text displayName) {

    public boolean accepts(ItemStack stack) {
        return validator != null && validator.test(stack);
    }
}