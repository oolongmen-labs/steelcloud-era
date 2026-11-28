package com.steelcloud.core.api.economy;

import java.util.UUID;

public class NoSuchAccountException extends IllegalArgumentException {
    private final UUID playerId;

    public NoSuchAccountException(UUID playerId) {
        super("No economy account for player: " + playerId);
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return playerId;
    }
}