package io.github._7isenko.aegis.discord.core;

import io.github._7isenko.aegis.discord.DiscordMessageListener;
import io.github._7isenko.aegis.discord.ReactListener;
import io.github._7isenko.aegis.discord.utils.MemberCollector;

public class ListenerController extends DiscordColleague {
    private final DiscordMessageListener discordMessageListener;
    private ReactListener reactListener = null;

    public ListenerController(DiscordMediator mediator) {
        super(mediator);
        this.discordMessageListener = new DiscordMessageListener(mediator);
        mediator.getJda().addEventListener(discordMessageListener);
    }

    public DiscordMessageListener getDiscordMessageListener() {
        return discordMessageListener;
    }

    public void startReactListener(String messageId, MemberCollector collector) {
        stopReactListener();
        reactListener = new ReactListener(messageId, collector);
        mediator.getJda().addEventListener(reactListener);
    }

    public void stopReactListener() {
        if (reactListener != null) {
            mediator.getJda().removeEventListener(reactListener);
            reactListener = null;
        }
    }

}
