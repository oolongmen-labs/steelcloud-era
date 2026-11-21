package com.steelcloud.core.api.train;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;
import java.util.UUID;

public interface TrainStorageService {
    /** 收纳一节车厢/车头。传入整列编组 UUID 时返回 empty。 */
    Optional<StoredTrain> capture(ServerWorld world, UUID trainId);

    boolean release(ServerWorld world, BlockPos railPos, StoredTrain stored);

    boolean validate(StoredTrain stored);
}