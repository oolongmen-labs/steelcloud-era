package com.steelcloud.core.impl.content.materials;

import net.minecraft.item.Item;

/**
 * 共享材料注册位。具体 Item 子类、模型、材质由内容模块提供；
 * 核心只声明注册位，由内容模块在各自 onInitialize 阶段完成注册。
 */
public final class SteelCloudMaterials {
    private SteelCloudMaterials() {}

    public static Item STEEL_INGOT;
    public static Item STEEL_PLATE;
    public static Item STEEL_BEARING;

    public static void register() {
        // 留空：由内容模块实现具体注册
    }
}