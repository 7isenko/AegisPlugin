package io.github._7isenko.aegis.discord;

import io.github._7isenko.aegis.discord.utils.MemberCollector;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ReactListener extends ListenerAdapter {
    private final String messageId;
    private final MemberCollector collector;

    public ReactListener(String messageId, MemberCollector collector) {
        this.messageId = messageId;
        this.collector = collector;
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        String eventMessageId = event.getMessageId();
        if (eventMessageId.equals(messageId)) {
            collector.addMember(event.getMember());
        }
    }

}
