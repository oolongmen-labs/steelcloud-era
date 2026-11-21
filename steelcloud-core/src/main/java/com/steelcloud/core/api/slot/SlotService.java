package com.steelcloud.core.api.slot;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Optional;

public interface SlotService {
    void registerSlot(SlotDefinition definition);
    Optional<SlotDefinition> getSlot(Identifier id);
    Collection<SlotDefinition> getAllSlots();

    /** 返回副本，调用方修改不影响内部存储。 */
    ItemStack getSlotItem(ServerPlayerEntity player, Identifier slotId);

    boolean setSlotItem(ServerPlayerEntity player, Identifier slotId, ItemStack stack);

    SlotOperationResult trySetSlotItem(ServerPlayerEntity player, Identifier slotId, ItemStack stack);
}