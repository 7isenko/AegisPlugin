package io.github._7isenko.aegis;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class DiscordEventMessageListener extends ListenerAdapter {
    private DiscordManager dm;

    public DiscordEventMessageListener(DiscordManager dm) {
        super();
        this.dm = dm;
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        Message message = event.getMessage();
        String rawMessage = message.getContentRaw();
        Guild guild = event.getGuild();
        String keyword = Aegis.config.getString("command");
        if (!event.getChannel().equals(dm.getWhitelistChannel()))
            return;
        if (rawMessage.startsWith("!" + keyword)) {
            try {
                String nickname = rawMessage.split(" ")[1];

                dm.register(nickname);
                event.getMessage().addReaction("\uD83D\uDC4D").queue(); // 👍
                guild.addRoleToMember(event.getMember(), dm.getMemberRole()).queue();
            } catch (IndexOutOfBoundsException e) {
                badRequest(message, "ввел никнейм неправильно.");
            } catch (NullPointerException e) {
                badRequest(message, "ввел никнейм, который не существует.");
            }
        } else badRequest(message, "вместо !" + keyword + " написал " + rawMessage);
    }

    private void badRequest(Message message, String text) {
        Member member = message.getMember();
        assert member != null;
        dm.getControlChannel().sendMessage("<@" + member.getId() + ">" + " " + text).queue();
        message.addReaction("\uD83D\uDC4E").queue(); // 👎
        member.kick(text).queue();
    }
}