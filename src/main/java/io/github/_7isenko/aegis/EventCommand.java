package io.github._7isenko.aegis;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class EventCommand implements CommandExecutor {
    String action;
    DiscordManager dm;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        dm = DiscordManager.getInstance();
        try {
            action = args[0];
            if (action == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        if (action.equals("start")) {
            dm.turnOnListener();
            sender.sendMessage("Бот запущен");
        } else if (action.equals("stop")) {
            UUIDRegisterListener.clearWhitelist();
            dm.turnOffListener();
            sender.sendMessage("Бот выключен, вайтлист очищен");
        } else if (action.equals("kick")) {
            dm.kickWithoutRoles();
            sender.sendMessage("Игроки без ролей были выгнаны");
        } else {
            sender.sendMessage("Команда была введена неправильно");
            return false;
        }
        return true;
    }
}
