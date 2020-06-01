package io.github._7isenko.aegis;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class UUIDRegisterEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final java.util.UUID UUID;

    public UUIDRegisterEvent(UUID uuid) {
        this.UUID = uuid;
    }

    public java.util.UUID getUUID() {
        return UUID;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
