package io.github._7isenko.aegis.discord.core;

import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ChatController extends DiscordColleague {

    private final TextChannel controlChannel;
    private final TextChannel mainChannel;
    private final TextChannel whitelistChannel;

    public ChatController(DiscordMediator mediator) {
        super(mediator);
        controlChannel = getChannel("control");
        mainChannel = getChannel("main");
        whitelistChannel = getChannel("whitelist");
    }

    public void sendToControlChannel(String message) {
        controlChannel.sendMessage(message).queue();
    }

    public void sendToMainChannel(String message) {
        mainChannel.sendMessage(message).queue();
    }

    public void sendToWhitelistChannel(String message) {
        whitelistChannel.sendMessage(message).queue();
    }

    public TextChannel getControlChannel() {
        return controlChannel;
    }

    public TextChannel getMainChannel() {
        return mainChannel;
    }

    public TextChannel getWhitelistChannel() {
        return whitelistChannel;
    }

    @NotNull
    public TextChannel getChannel(@NotNull String name) {
        return searchChannel(Collections.singletonList(name));
    }

    @NotNull
    private TextChannel searchChannel(@NotNull List<String> names) {
        if (names.isEmpty()) throw new IllegalArgumentException("Empty list of names");
        for (String name : names) {
            List<TextChannel> channels = mediator.getGuild().getTextChannelsByName(name, true);
            if (!channels.isEmpty()) {
                return channels.get(0);
            }
        }
        return mediator.getGuild().createTextChannel(names.get(0)).complete();
    }
}
