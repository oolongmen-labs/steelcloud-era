package com.steelcloud.core.api.currency;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

/**
 * 物理货币支付核心入口。采用净转移模型：对每种面额计算净转移量，
 * 一次性完成双方物品栏变更，不采用"先付再找"的迭代流程。
 */
public interface PaymentService {
    /** 玩家 ↔ 玩家。 */
    PaymentResult pay(ServerPlayerEntity payer, ServerPlayerEntity payee,
                      Identifier currencyId, long amount);

    /** 玩家 ↔ 任意物品栏（NPC、交易站职员、虚拟商人）。 */
    PaymentResult payToInventory(ServerPlayerEntity payer, PlayerInventory payeeInv,
                                 Vec3d payeeDropPos, Identifier currencyId, long amount);

    /** 玩家 → 系统（钱消失）。 */
    PaymentResult payToVoid(ServerPlayerEntity payer, Identifier currencyId,
                            long amount, String reason);

    /** 系统 → 玩家（凭空生成）。 */
    PaymentResult refund(ServerPlayerEntity payee, Identifier currencyId,
                         long amount, String reason);

    boolean canPay(ServerPlayerEntity payer, Identifier currencyId, long amount);
}