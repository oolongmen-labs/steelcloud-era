package com.steelcloud.core.impl.currency;

import com.steelcloud.core.api.currency.CurrencySpec;
import com.steelcloud.core.api.currency.Denomination;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import java.util.List;

/** 从 Inventory 扫描出的各面额可用张数。 */
public final class Wallet {
    private final CurrencySpec spec;
    private final int[] stocks;

    private Wallet(CurrencySpec spec, int[] stocks) {
        this.spec = spec;
        this.stocks = stocks;
    }

    public static Wallet scan(Inventory inv, CurrencySpec spec) {
        List<Denomination> denoms = spec.denominations();
        int[] stocks = new int[denoms.size()];
        for (int slot = 0; slot < inv.size(); slot++) {
            ItemStack stack = inv.getStack(slot);
            if (stack.isEmpty()) continue;
            for (int i = 0; i < denoms.size(); i++) {
                if (stack.isOf(denoms.get(i).item())) {
                    stocks[i] += stack.getCount();
                    break;
                }
            }
        }
        return new Wallet(spec, stocks);
    }

    public CurrencySpec spec() { return spec; }

    public int[] stocks() { return stocks.clone(); }

    public int[] denomValues() {
        List<Denomination> denoms = spec.denominations();
        int[] values = new int[denoms.size()];
        for (int i = 0; i < denoms.size(); i++) {
            values[i] = denoms.get(i).value();
        }
        return values;
    }

    public long totalValue() {
        List<Denomination> denoms = spec.denominations();
        long sum = 0;
        for (int i = 0; i < denoms.size(); i++) {
            sum += (long) denoms.get(i).value() * stocks[i];
        }
        return sum;
    }
}