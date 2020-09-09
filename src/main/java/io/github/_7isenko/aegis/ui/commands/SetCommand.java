package io.github._7isenko.aegis.ui.commands;

import io.github._7isenko.aegis.minecraft.UUIDHelper;
import io.github._7isenko.aegis.minecraft.WhitelistManager;
import io.github._7isenko.aegis.ui.Command;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SetCommand implements Command {
    private final WhitelistManager manager;

    public SetCommand() {
        this.manager = WhitelistManager.getInstance();
    }

    @Override
    public String call(@NotNull String... args) {
        try {
            String username = args[0];
            if (username.equals("null"))
                return "Пустой никнейм";
            UUID uuid = UUIDHelper.getUuid(username);
            manager.addToWhitelist(uuid);
        } catch (Exception e) {
            return "Игрока с таким ником не существует";
        }
        return null;
    }
}
