package com.steelcloud.core.api.train;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public record TrainSpec(
        Identifier bodyType,
        int carCount,
        double initialSpeed,
        Direction facing,
        NbtCompound customData) {

    public TrainSpec {
        if (bodyType == null) throw new IllegalArgumentException("bodyType must not be null");
        if (carCount < 1) throw new IllegalArgumentException("carCount must be >= 1");
        if (initialSpeed < 0) throw new IllegalArgumentException("initialSpeed must be >= 0");
        if (facing == null) throw new IllegalArgumentException("facing must not be null");
        customData = customData == null ? new NbtCompound() : customData.copy();
    }

    public static TrainSpec of(Identifier bodyType, int carCount) {
        return new TrainSpec(bodyType, carCount, 0.0, Direction.NORTH, new NbtCompound());
    }
}