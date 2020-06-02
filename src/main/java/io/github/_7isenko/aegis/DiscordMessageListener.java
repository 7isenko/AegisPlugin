package io.github._7isenko.aegis;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class DiscordMessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        Guild guild = event.getGuild();
        String keyword = Aegis.config.getString("command");
        if (!event.getChannel().equals(guild.getTextChannelById(Aegis.config.getString("whitelist_channel_id"))))
            return;
        if (message.startsWith("!" + keyword)) {
            try {
                String nickname = message.split(" ")[1];
                UUIDGetter.getInstance().registerPlayerByNickname(nickname);
                event.getMessage().addReaction("\uD83D\uDC4D").queue(); // 👍
                guild.addRoleToMember(event.getMember(), event.getGuild().getRoleById(Aegis.config.getString("member_role_id"))).queue();
            } catch (IndexOutOfBoundsException e) {
                badRequest(event, "ввел никнейм неправильно.");
            } catch (NullPointerException e) {
                badRequest(event, "ввел никнейм, который не существует.");
            }
        } else badRequest(event, "вместо !" + keyword + " написал " + message);
    }

    private void badRequest(MessageReceivedEvent event, String message) {
        Member member = event.getMember();
        event.getGuild().getTextChannelById(Aegis.config.getString("notifications_channel_id")).sendMessage("<@" + member.getId() + ">" + " " + message).queue();
        event.getMessage().addReaction("\uD83D\uDC4E").queue(); // 👎
        member.kick(message).queue();
    }
}