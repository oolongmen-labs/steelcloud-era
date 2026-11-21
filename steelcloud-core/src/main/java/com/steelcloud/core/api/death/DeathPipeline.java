package com.steelcloud.core.api.death;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

public interface DeathPipeline {
    void registerHandler(DeathHandler handler);

    /**
     * 按 priority 升序调用 handler，遇到第一个返回 Optional.of(...) 的终止；
     * 全部返回 empty 时返回 Proceed。
     */
    DeathResult process(ServerPlayerEntity player, DamageSource source);
}