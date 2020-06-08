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
        if (args.length == 0)
            return false;
        action = args[0];
        switch (action) {
            case "start":
                dm.start();
                sender.sendMessage("Бот запущен");
                break;
            case "stop":
                dm.stop();
                sender.sendMessage("Бот выключен, вайтлист очищен");
                break;
            case "kick":
                dm.kickWithoutRoles();
                sender.sendMessage("Игроки без ролей были выгнаны");
                break;
            case "emote":
                dm.startEmoteMode();
                sender.sendMessage("Дропаю мессадж");
                break;
            case "add":
                if (dm.isEmoteMode()) {
                    int num = Integer.parseInt(args[1]);
                    dm.setChosenRoles(num);
                    sender.sendMessage("Запущен процесс добавления " + num + " игроков");
                } else sender.sendMessage("Сначала напиши !emote и подожди");
                break;
            default:
                sender.sendMessage("Команда была введена неправильно");
                return false;
        }
        return true;
    }
}
