package io.github._7isenko.aegis.minecraft;

import io.github._7isenko.aegis.ui.CommandInvoker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class EventCommandExecutor implements CommandExecutor {
    private CommandInvoker invoker;

    public EventCommandExecutor(CommandInvoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0)
            return false;
        // TODO: при вызове в майнкрафте парсить все \n как новые сообщения, а не строки и подкрасить в золотой
        String message = invoker.execute(args[0], Arrays.copyOfRange(args, 1, args.length));
        sender.sendMessage(message);
        return true;
    }
}
