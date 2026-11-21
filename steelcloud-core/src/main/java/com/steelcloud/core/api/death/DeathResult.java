package com.steelcloud.core.api.death;

public sealed interface DeathResult {
    /** 取消死亡事件，将玩家血量置为 remainingHealth。 */
    record Cancelled(float remainingHealth) implements DeathResult {}
    /** 放行走正常死亡掉落。 */
    record Proceed() implements DeathResult {}

    static DeathResult cancel(float hp) { return new Cancelled(hp); }
    static DeathResult proceed()        { return new Proceed(); }
}