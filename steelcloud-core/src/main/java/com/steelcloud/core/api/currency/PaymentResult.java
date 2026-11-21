package com.steelcloud.core.api.currency;

import net.minecraft.util.Identifier;

public sealed interface PaymentResult {
    record Success(long amount) implements PaymentResult {}
    record InsufficientFunds(long required, long available) implements PaymentResult {}
    record CannotMakeChange(long amount, long maxPayable) implements PaymentResult {}
    record AmountTooLarge(long amount, long limit) implements PaymentResult {}
    record CurrencyNotRegistered(Identifier id) implements PaymentResult {}
    record InvalidAmount(long amount) implements PaymentResult {}
}