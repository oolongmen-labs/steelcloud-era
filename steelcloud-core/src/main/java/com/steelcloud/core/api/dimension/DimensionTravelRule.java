package com.steelcloud.core.api.dimension;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface DimensionTravelRule {
    Identifier getId();

    boolean shouldApply(ServerPlayerEntity player, ServerWorld from, Vec3d pos);

    Target resolve(ServerPlayerEntity player, ServerWorld from, Vec3d pos);

    record Target(RegistryKey<World> dimension, Vec3d position) {}
}