package com.steelcloud.core.api.train;

public interface TrainLifecycleListener {
    default void onSpawn(TrainSnapshot snapshot) {}
    default void onArrive(TrainSnapshot snapshot, String stationName) {}
    default void onDespawn(TrainSnapshot snapshot) {}
}