package com.steelcloud.core.api.persistence;

import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;

/**
 * 核心持久化基类。getId() 建议与 PersistentStateManager.getOrCreate 的 name 参数一致，
 * 便于核心注册表与调试命令定位。
 */
public abstract class SteelCloudPersistentState extends PersistentState {
    public abstract Identifier getId();
}