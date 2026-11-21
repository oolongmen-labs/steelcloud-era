package com.steelcloud.core.impl.currency;

import com.steelcloud.core.api.currency.CurrencyService;
import com.steelcloud.core.api.currency.CurrencySpec;
import com.steelcloud.core.api.currency.Denomination;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class CurrencyServiceImpl implements CurrencyService {
    private final Map<Identifier, CurrencySpec> currencies = new ConcurrentHashMap<>();

    @Override
    public void registerCurrency(CurrencySpec spec) {
        CurrencySpec prev = currencies.putIfAbsent(spec.id(), spec);
        if (prev != null) {
            throw new IllegalStateException("Currency already registered: " + spec.id());
        }
    }

    @Override
    public Optional<CurrencySpec> getCurrency(Identifier id) {
        return Optional.ofNullable(currencies.get(id));
    }

    @Override
    public Collection<CurrencySpec> getAllCurrencies() {
        return Collections.unmodifiableCollection(currencies.values());
    }

    @Override
    public Optional<Denomination> getDenomination(Identifier currencyId, Item item) {
        CurrencySpec spec = currencies.get(currencyId);
        if (spec == null) return Optional.empty();
        for (Denomination d : spec.denominations()) {
            if (d.item() == item) return Optional.of(d);
        }
        return Optional.empty();
    }

    @Override
    public long getBalance(PlayerEntity player, Identifier currencyId) {
        CurrencySpec spec = currencies.get(currencyId);
        if (spec == null) return 0L;
        Inventory inv = player.getInventory();
        Wallet wallet = Wallet.scan(inv, spec);
        return wallet.totalValue();
    }
}