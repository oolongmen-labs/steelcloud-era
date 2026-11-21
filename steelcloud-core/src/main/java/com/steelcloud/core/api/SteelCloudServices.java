package com.steelcloud.core.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务注册表。所有 API 接口通过此入口获取实现，不直接 new 实现类。
 * 必需依赖用 get()（fail-fast），可选依赖用 find()。
 */
public final class SteelCloudServices {
    private static final Map<Class<?>, Object> SERVICES = new ConcurrentHashMap<>();

    private SteelCloudServices() {}

    public static <T> void register(Class<T> type, T impl) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(impl, "impl");
        Object prev = SERVICES.putIfAbsent(type, impl);
        if (prev != null) {
            throw new IllegalStateException("Service already registered: " + type.getName());
        }
    }

    public static <T> T get(Class<T> type) {
        Object impl = SERVICES.get(type);
        if (impl == null) {
            throw new ServiceUnavailableException(type);
        }
        return type.cast(impl);
    }

    public static <T> Optional<T> find(Class<T> type) {
        Object impl = SERVICES.get(type);
        return Optional.ofNullable(type.cast(impl));
    }

    public static boolean isRegistered(Class<?> type) {
        return SERVICES.containsKey(type);
    }

    /** 显式注册 stub（仅限 dev/测试环境，核心不自动调用）。 */
    public static <T> void registerStub(Class<T> type, T stub) {
        register(type, stub);
    }

    /** 启动自检：缺任一服务则抛出 ServiceUnavailableException，附完整缺失列表。 */
    public static void requireAll(Class<?>... types) {
        List<Class<?>> missing = new ArrayList<>();
        for (Class<?> type : types) {
            if (!SERVICES.containsKey(type)) missing.add(type);
        }
        if (!missing.isEmpty()) {
            throw new ServiceUnavailableException(missing);
        }
    }

    /** 仅测试/关机清理用。 */
    public static void clear() {
        SERVICES.clear();
    }
}