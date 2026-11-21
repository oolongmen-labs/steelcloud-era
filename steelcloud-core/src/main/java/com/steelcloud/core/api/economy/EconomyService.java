package com.steelcloud.core.api.economy;

import java.util.UUID;

public interface EconomyService {
    /** 未注册 UUID 抛 NoSuchAccountException。 */
    long getBalance(UUID playerId);

    boolean withdraw(UUID playerId, long amount, String reason);
    boolean deposit(UUID playerId, long amount, String reason);

    /** 从发送方扣 amount，收款方收 (amount - tax)，tax = 5% * amount（向下取整）。 */
    TransferResult transfer(UUID from, UUID to, long amount);

    DailyLimit getDailyLimit(UUID playerId);

    /** 返回是否实际发生了重置。 */
    boolean resetDailyLimitsIfNeeded();
}