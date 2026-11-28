package com.steelcloud.core.impl.content.materials;

import com.steelcloud.core.api.SteelCloudRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;

/**
 * 共享材料注册位。具体 Item 子类、模型、材质由内容模块提供；
 * 核心只声明注册位，由内容模块在各自 onInitialize 阶段完成注册。
 */
public final class SteelCloudMaterials {
    private SteelCloudMaterials() {}

    public static Item STEEL_INGOT;
    public static Item STEEL_PLATE;
    public static Item STEEL_BEARING;

    /**
     * 辉夜石（glorynight_stone）。
     *
     * 目前由核心直接注册：BottleCapMod 的辉夜石瓶盖配方依赖这个物品存在，
     * 而归属它的内容模块尚未建立。等该模块落地后把实现搬过去，
     * 核心只保留注册位即可（与 STEEL_INGOT 等保持一致的演进路径）。
     */
    public static Item GLORYNIGHT_STONE;

    public static void register() {
        GLORYNIGHT_STONE = SteelCloudRegistry.register(Registries.ITEM, "glorynight_stone",
                new Item(new Item.Settings()));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                .register(entries -> entries.add(GLORYNIGHT_STONE));
    }
}
