package com.steelcloud.core.api.dimension;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface DimensionTravelService {
    boolean teleport(ServerPlayerEntity player, RegistryKey<World> dimension, Vec3d targetPos);

    /** 玩家无坐骑时退化为普通 teleport。 */
    boolean teleportWithMount(ServerPlayerEntity player, RegistryKey<World> dimension, Vec3d targetPos);

    void registerRule(DimensionTravelRule rule);
}