package com.steelcloud.core.api.train;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface TrainService {
    /** 生成一列车；失败（无轨道、位置非法、实现层未加载）返回 empty。 */
    Optional<UUID> spawnTrain(ServerWorld world, BlockPos pos, TrainSpec spec);

    boolean despawnTrain(UUID trainId);

    Optional<TrainSnapshot> getTrain(UUID trainId);

    /** radius 单位为方块（blocks）。 */
    List<TrainSnapshot> getTrainsNear(ServerWorld world, BlockPos pos, double radius);

    void addListener(TrainLifecycleListener listener);

    /** 模块卸载时必须调用，防止监听器泄漏。 */
    void removeListener(TrainLifecycleListener listener);

    /** 返回 train 模组支持的列车类型 id 集合；无类型概念时返回空集。 */
    default Set<Identifier> getAvailableBodyTypes() { return Set.of(); }
}