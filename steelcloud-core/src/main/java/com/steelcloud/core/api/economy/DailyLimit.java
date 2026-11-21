package com.steelcloud.core.api.economy;

public record DailyLimit(
        long purchaseLimit,
        long purchaseUsed,
        long saleLimit,
        long saleUsed) {

    public long purchaseRemaining() { return Math.max(0, purchaseLimit - purchaseUsed); }
    public long saleRemaining()     { return Math.max(0, saleLimit - saleUsed); }
    public boolean purchaseExhausted() { return purchaseRemaining() == 0; }
    public boolean saleExhausted()     { return saleRemaining() == 0; }
}