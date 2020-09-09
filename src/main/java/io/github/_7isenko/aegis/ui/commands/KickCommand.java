package io.github._7isenko.aegis.ui.commands;

import io.github._7isenko.aegis.discord.core.DiscordMediator;
import io.github._7isenko.aegis.discord.core.DiscordColleague;
import io.github._7isenko.aegis.ui.Command;
import org.jetbrains.annotations.Nullable;

public class KickCommand extends DiscordColleague implements Command {
    public KickCommand(DiscordMediator mediator) {
        super(mediator);
    }

    @Override
    public String call(@Nullable String... args) {
        mediator.getMemberController().kickStrangers();
        return "Удаление участников без ролей запущено";
    }
}