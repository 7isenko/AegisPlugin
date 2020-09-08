package io.github._7isenko.aegis.ui.commands;

import io.github._7isenko.aegis.discord.core.DiscordMediator;
import io.github._7isenko.aegis.discord.core.DiscordColleague;
import io.github._7isenko.aegis.discord.utils.DiscordRandomizer;
import io.github._7isenko.aegis.ui.Command;
import io.github._7isenko.aegis.utils.StringsTool;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class AddCommand extends DiscordColleague implements Command  {

    public AddCommand(DiscordMediator mediator) {
        super(mediator);
    }
    @Override
    public String call(@NotNull String... args) {
        mediator.getMessageListener().setWhitelistOn(true);
        int amount = Integer.parseInt(args[0].replaceAll("\\D", ""));
        setChosenRoles(amount);
        return "Выдача ролей " + amount + " людям началась.";
    }

    public void setChosenRoles(int amount) {
        Set<Member> members = DiscordRandomizer.getRandomMembers(mediator.getGuild(), amount, null);
        members.forEach(m -> mediator.getRoleController().addPickedRole(m));
        mediator.getChatController().sendToMainChannel("Ребята, которых выбрал бот:");
        StringsTool.membersToMentions(members).forEach(m -> mediator.getChatController().sendToMainChannel(m));
    }
}