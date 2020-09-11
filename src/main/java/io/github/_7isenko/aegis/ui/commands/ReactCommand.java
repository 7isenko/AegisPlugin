package io.github._7isenko.aegis.ui.commands;

import io.github._7isenko.aegis.discord.core.DiscordColleague;
import io.github._7isenko.aegis.discord.core.DiscordMediator;
import io.github._7isenko.aegis.discord.utils.MemberCollector;
import io.github._7isenko.aegis.ui.Command;
import io.github._7isenko.aegis.ui.CommandInvoker;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.managers.EmoteManager;

import java.util.List;
import java.util.Random;

public class ReactCommand extends DiscordColleague implements Command {
    public ReactCommand(DiscordMediator mediator) {
        super(mediator);
    }

    @Override
    public String call(String... args) {
        Message message = mediator.getChatController().getMainChannel().sendMessage(getRandomString(mediator.getConfig().getEmoteModeMessages())).complete();
        message.addReaction("U+270B").queue(); // ✋

        MemberCollector collector = mediator.getMemberController().createMemberCollector("react");
        mediator.getListenerController().startReactListener(message.getId(), collector);

        return "Сообщение с реакцией отправлено.";
    }

    public static String getRandomString(List<String> s) {
        if (s != null && !s.isEmpty())
            return s.get(new Random().nextInt(s.size()));
        else return "Хай файв!";
    }
}
