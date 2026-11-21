package com.steelcloud.core.api.currency;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Optional;

public interface CurrencyService {
    /** 重复 id 抛 IllegalStateException。 */
    void registerCurrency(CurrencySpec spec);

    Optional<CurrencySpec> getCurrency(Identifier id);
    Collection<CurrencySpec> getAllCurrencies();

    /** 按物品查找其所属货币面额；非货币物品返回 empty。 */
    Optional<Denomination> getDenomination(Identifier currencyId, Item item);

    /** 扫描物品栏求和；只读操作。 */
    long getBalance(PlayerEntity player, Identifier currencyId);
}