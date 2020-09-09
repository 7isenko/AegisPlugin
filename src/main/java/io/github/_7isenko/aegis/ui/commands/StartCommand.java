package io.github._7isenko.aegis.ui.commands;

import io.github._7isenko.aegis.discord.core.DiscordMediator;
import io.github._7isenko.aegis.discord.core.DiscordColleague;
import io.github._7isenko.aegis.ui.Command;
import org.jetbrains.annotations.Nullable;

public class StartCommand extends DiscordColleague implements Command {
    public StartCommand(DiscordMediator mediator) {
        super(mediator);
    }

    @Override
    public String call(@Nullable String... args) {
        if (!mediator.getMessageListener().isWhitelistOn()) {
            mediator.getMessageListener().setWhitelistOn(true);
            mediator.getChatController().sendToWhitelistChannel("Начинайте писать !set ник");
        }
        return "Слушаю whitelist.";
    }
}
