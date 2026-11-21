package com.steelcloud.core.api.economy;

public sealed interface TransferResult {
    /** sent = received + tax。 */
    record Success(long sent, long tax, long received) implements TransferResult {}
    record InsufficientFunds(long required, long available) implements TransferResult {}
    record Rejected(String reason) implements TransferResult {}

    static TransferResult success(long sent, long tax, long received) {
        return new Success(sent, tax, received);
    }
}