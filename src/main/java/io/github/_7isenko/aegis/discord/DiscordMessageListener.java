package io.github._7isenko.aegis.discord;

import io.github._7isenko.aegis.discord.core.DiscordMediator;
import io.github._7isenko.aegis.ui.CommandInvoker;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class DiscordMessageListener extends ListenerAdapter {
    private final DiscordMediator dm;
    private final CommandInvoker invoker;
    private boolean whitelistOn;

    public DiscordMessageListener(DiscordMediator dm) {
        this.dm = dm;
        this.invoker = CommandInvoker.getInstance();
        this.whitelistOn = false;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (!event.isFromType(ChannelType.TEXT)) {
            return; // why, JDA developers
        }

        Guild guild = event.getGuild();
        Message message = event.getMessage();

        if (guild != dm.getGuild())
            return;

        String[] words = message.getContentRaw().replaceAll("\\s+", " ").replaceAll("[\\\\]", "").replaceAll("/", "").split(" ");

        if (event.getChannel().equals(dm.getChatController().getWhitelistChannel()) && whitelistOn) {
            if (words[0].equalsIgnoreCase("!set") || words[0].equalsIgnoreCase("/set")) {

                String[] nick = Arrays.copyOfRange(words, 1, 2);
                String result = invoker.execute("set", nick);

                if (result == null) {
                    message.addReaction("\uD83D\uDC4D").queue(); // 👍
                    dm.getRoleController().addParticipantRole(event.getMember());
                    return;
                }
                message.addReaction("\uD83D\uDC4E").queue(); // 👎
                dm.getLogger().info(Objects.requireNonNull(event.getMember()).getAsMention() + ": " + result);
            }
            dm.getMemberController().kick(event.getMember());
        }

        if (event.getChannel().equals(dm.getChatController().getControlChannel())) {
            String command = words[0];

            if (command.startsWith("!")) {
                try {
                    String res = invoker.execute(command.substring(1), Arrays.copyOfRange(words, 1, words.length));
                    if (res != null)
                        dm.getLogger().info(res);
                } catch (Exception e) {
                    dm.getLogger().logException(e);
                }
            }
        }
    }

    public boolean isWhitelistOn() {
        return whitelistOn;
    }

    public void setWhitelistOn(boolean whitelistOn) {
        this.whitelistOn = whitelistOn;
    }

}