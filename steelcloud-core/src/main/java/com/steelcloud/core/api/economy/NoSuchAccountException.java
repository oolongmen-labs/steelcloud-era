package com.steelcloud.core.api.economy;

import java.util.UUID;

public class NoSuchAccountException extends IllegalArgumentException {
    private final UUID playerId;

    public NoSuchAccountException(UUID playerId) {
        super("Error: selected player doesn't exist: " + playerId);
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return playerId;
    }
}