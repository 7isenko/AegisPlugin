package io.github._7isenko.aegis.discord.core;

import java.io.PrintWriter;
import java.io.StringWriter;

public class DiscordLogger extends DiscordColleague {

    public DiscordLogger(DiscordMediator mediator) {
        super(mediator);
    }

    /**
     * Sends given message to the control channel
     *
     * @param message Your formatted message
     */
    public void info(String message) {
        mediator.getChatController().sendToControlChannel(message);
    }

    public void logException(Exception e) {
        mediator.getChatController().sendToControlChannel("Вылезла ошибка: " + e.getMessage());
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        mediator.getChatController().sendToControlChannel("```" + sw.toString() + "```");
    }
}
