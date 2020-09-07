package io.github._7isenko.aegis.discord;

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
        try {
            String rawMessage = message.getContentRaw();
            Guild guild = event.getGuild();
            if (!event.getChannel().equals(dm.getWhitelistChannel()))
                return;

            // Remove a "chosen" role
            guild.removeRoleFromMember(event.getMember(), dm.getChosenRole()).queue();


            String keyword = "set";
            if (rawMessage.startsWith("!" + keyword)) {
                try {
                    String nickname = rawMessage.split(" ")[1];

                    dm.register(nickname);
                    event.getMessage().addReaction("\uD83D\uDC4D").queue(); // 👍

                    // Add a "member" role
                    guild.addRoleToMember(event.getMember(), dm.getMemberRole()).queue();

                } catch (IndexOutOfBoundsException e) {
                    badRequest(message, "ввел никнейм неправильно.");
                } catch (NullPointerException e) {
                    badRequest(message, "ввел несуществующий ник");
                }
            } else badRequest(message, "вместо !" + keyword + " написал " + rawMessage);
        } catch (Exception e) {
            badRequest(message, e.getMessage());
        }
    }

    private void badRequest(Message message, String text) {
        Member member = message.getMember();
        if (member != null) {
            dm.getControlChannel().sendMessage(member.getAsMention() + " " + text).queue();
            message.addReaction("\uD83D\uDC4E").queue(); // 👎
            if (dm.isAllowKick())
                member.kick(text).queue();
        } else {
            dm.getControlChannel().sendMessage("Туда его: " + text);
        }
    }

}