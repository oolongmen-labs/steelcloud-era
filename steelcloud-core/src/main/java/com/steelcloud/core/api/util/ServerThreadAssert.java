package com.steelcloud.core.api.util;

import net.minecraft.server.MinecraftServer;

/** 服务端线程断言。所有标注"服务端线程调用"的接口方法应在入口调用 check。 */
public final class ServerThreadAssert {
    private ServerThreadAssert() {}

    public static void check(MinecraftServer server) {
        if (!server.isOnThread()) {
            throw new IllegalStateException("Must be called on server thread");
        }
    }
}