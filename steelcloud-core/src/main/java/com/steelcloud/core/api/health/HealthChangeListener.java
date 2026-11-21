package com.steelcloud.core.api.health;

import net.minecraft.server.network.ServerPlayerEntity;

public interface HealthChangeListener {
    void onHealthChanged(ServerPlayerEntity player, float oldValue, float newValue, String reason);
}