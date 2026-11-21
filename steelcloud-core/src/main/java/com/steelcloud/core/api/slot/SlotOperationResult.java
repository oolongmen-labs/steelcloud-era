package com.steelcloud.core.api.slot;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public sealed interface SlotOperationResult {
    record Success() implements SlotOperationResult {}
    record UnknownSlot(Identifier slotId) implements SlotOperationResult {}
    record InvalidItem(ItemStack stack) implements SlotOperationResult {}
    record Conflict(Identifier conflictingSlot) implements SlotOperationResult {}

    default boolean isSuccess() { return this instanceof Success; }
}