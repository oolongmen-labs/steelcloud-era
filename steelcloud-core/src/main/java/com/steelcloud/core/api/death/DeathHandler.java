package com.steelcloud.core.api.death;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public interface DeathHandler {
    /** 数值越小越先执行。 */
    int priority();

    /** Optional.empty() 表示不处理，交给下一个。 */
    Optional<DeathResult> handle(ServerPlayerEntity player, DamageSource source);
}