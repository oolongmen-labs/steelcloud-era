package com.steelcloud.core.api.train;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

public record TrainSnapshot(
        UUID trainId,
        Identifier bodyType,
        RegistryKey<World> dimension,
        Vec3d position,
        float yaw,
        double speed,
        TrainState state) {

    public enum TrainState { IDLE, MOVING, ARRIVED, DESPAWNED }
}