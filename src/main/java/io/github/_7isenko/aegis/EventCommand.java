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
        action = args[0];
        if (action.equals("start")) {
            dm.turnOnListener();
            dm.turnOnChannel();
            sender.sendMessage("Бот запущен");
        } else if (action.equals("stop")) {
            UUIDRegisterListener.clearWhitelist();
            dm.turnOffListener();
            dm.turnOffChannel();
            sender.sendMessage("Бот выключен, вайтлист очищен");
        } else {
            sender.sendMessage("Команда была введена неправильно");
            return false;
        }
        return true;
    }
}
