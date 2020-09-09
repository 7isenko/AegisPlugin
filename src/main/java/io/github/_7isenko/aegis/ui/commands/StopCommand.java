package io.github._7isenko.aegis.ui.commands;

import io.github._7isenko.aegis.discord.core.DiscordMediator;
import io.github._7isenko.aegis.discord.core.DiscordColleague;
import io.github._7isenko.aegis.minecraft.WhitelistManager;
import io.github._7isenko.aegis.ui.Command;
import org.jetbrains.annotations.Nullable;


public class StopCommand extends DiscordColleague implements Command {
    public StopCommand(DiscordMediator mediator) {
        super(mediator);
    }

    @Override
    public String call(@Nullable String... args) {
        WhitelistManager.getInstance().clearWhitelist();
        if (mediator.getMessageListener().isWhitelistOn()) {
            mediator.getMessageListener().setWhitelistOn(false);
            mediator.getChatController().sendToWhitelistChannel("Всем спасибо, все свободны");
        }
        mediator.getRoleController().clearPartakers();
        return "Вайтлист очищен, роли забираю.";
    }
}
