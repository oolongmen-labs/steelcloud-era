package com.steelcloud.core.api.data;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public interface DataTableService {
    /** 重复注册同一 id 抛 IllegalStateException。 */
    <T> void registerTable(Identifier id, Class<T> type, DataTableCodec<T> codec);

    /** 类型不符抛 ClassCastException，附注册 type 与实际请求 type 的诊断。 */
    <T> Optional<T> getTable(Identifier id, Class<T> type);

    void reloadAll(MinecraftServer server);

    Set<Identifier> getLoadedTables();

    /** 每次成功 reload 递增；调用方可据此判断缓存是否陈旧。 */
    long getRevision(Identifier id);

    /** reload 后通知本次实际发生变化的表 id 集合。 */
    void addReloadListener(Consumer<Set<Identifier>> listener);
    void removeReloadListener(Consumer<Set<Identifier>> listener);
}