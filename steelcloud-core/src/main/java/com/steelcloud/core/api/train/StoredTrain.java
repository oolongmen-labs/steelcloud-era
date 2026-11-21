package com.steelcloud.core.api.train;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public interface StoredTrain {
    NbtCompound writeNbt(NbtCompound nbt);
    void readNbt(NbtCompound nbt);
    Identifier getTrainType();

    /** 格式版本，单调递增；validate 时与 train 模组支持的 [MIN_SUPPORTED, CURRENT] 范围比对。 */
    int getFormatVersion();
}