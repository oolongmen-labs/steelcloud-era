package com.steelcloud.core.api.economy;

import net.minecraft.item.ItemStack;

import java.util.OptionalLong;

public interface PriceTableService {
    OptionalLong getBuyPrice(ItemStack stack);
    OptionalLong getSellPrice(ItemStack stack);
    boolean isTradeable(ItemStack stack);

    /** 语义版本号，如 "1.0.3"，与 xlsx 源文件版本对应。 */
    String getTableVersion();
}