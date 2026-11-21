package com.steelcloud.core.api.health;

import net.minecraft.server.network.ServerPlayerEntity;

public interface HealthService {
    float getHealth(ServerPlayerEntity player);
    void setHealth(ServerPlayerEntity player, float value);
    void addHealth(ServerPlayerEntity player, float delta, String reason);
    HealthThreshold getThreshold(ServerPlayerEntity player);
    void addListener(HealthChangeListener listener);
    void removeListener(HealthChangeListener listener);
}