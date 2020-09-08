package io.github._7isenko.aegis.ui.commands;

import io.github._7isenko.aegis.ui.Command;
import org.jetbrains.annotations.Nullable;

public class HelpCommand implements Command {
    @Override
    public String call(@Nullable String... args) {
        return "start - запустить бота в whitelist-канале\n" +
                "stop - остановить и забрать выданные роли\n" +
                "add <число> - рандомно раздать роль нужному числу участников\n" +
                "kick - выгнать всех без ролей\n" +
                "set <ник> - добавить ник в вайтлист";
    }
}