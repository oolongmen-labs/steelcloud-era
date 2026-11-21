package com.steelcloud.core.api;

import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class SteelCloudRegistry {
    public static final String MOD_ID = "steelcloud";

    private SteelCloudRegistry() {}

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    /** 重复 ID 抛异常，与 Fabric Registry.register 行为一致。 */
    public static <T> T register(Registry<T> registry, String path, T value) {
        return Registry.register(registry, id(path), value);
    }
}