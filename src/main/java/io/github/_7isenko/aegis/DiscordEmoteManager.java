package io.github._7isenko.aegis;

import net.dv8tion.jda.api.entities.*;

import java.util.*;

public class DiscordEmoteManager {
    private final DiscordManager dm;
    private final TextChannel announceChannel;
    private Message emoteMessage;

    public DiscordEmoteManager(DiscordManager dm) {
        this.dm = dm;
        this.announceChannel = dm.getAnnounceChannel();
        sendEmoteMessage();
    }

    private String generateCoolMessage() {
        return "*ввввд-жуужу-жббжжужу*\nРобот запущен. Давайте пятюню!";
    }

    private void sendEmoteMessage() {
        announceChannel.sendMessage(generateCoolMessage()).queue(message -> {
            message.addReaction("✋").queue();
            emoteMessage = message;
        });
    }

    public List<Member> getEmotedMembers(int amount) {
        List<Member> members = new ArrayList<>();
        int[] i = {0};
        List<User> users = dm.getAnnounceChannel().retrieveReactionUsersById(emoteMessage.getId(), "✋").complete();
        Collections.shuffle(users);
        users.forEach((user) -> {
            try {
                Member member = dm.getGuild().getMember(user);
                assert member != null;
                if (i[0] < amount && !member.getRoles().contains(dm.getChosenRole()) && !member.getRoles().contains(dm.getMemberRole()) && !member.getUser().isBot()) {
                    members.add(member);
                    ++i[0];
                }
            } catch (Exception e) {
                Aegis.logger.info("Caught exception during getting members from emote: " + e.getMessage());
            }
        });
        return members;
    }
}


