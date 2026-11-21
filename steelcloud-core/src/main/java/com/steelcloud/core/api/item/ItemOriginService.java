package com.steelcloud.core.api.item;

import net.minecraft.item.ItemStack;

public interface ItemOriginService {
    /** 未知或空栈返回 UNKNOWN。 */
    ItemOrigin getOrigin(ItemStack stack);

    /** 空栈时 no-op。 */
    void markOrigin(ItemStack stack, ItemOrigin origin);

    /** 空栈返回 false。 */
    boolean isSellableToGovernment(ItemStack stack);
}