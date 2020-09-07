package io.github._7isenko.aegis.discord;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.*;

public class DiscordRandomManager {
    private final DiscordManager dm;
    private final TextChannel announceChannel;
    // TODO: брать из конфига
    private String[] messages;

    public DiscordRandomManager(DiscordManager dm) {
        this.dm = dm;
        this.announceChannel = dm.getAnnounceChannel();
    }

    private String generateCoolMessage() {
        return messages[new Random().nextInt(messages.length)];
    }

    public void sendAnnounceMessage() {
        announceChannel.sendMessage(generateCoolMessage()).queue();
    }

    public List<Member> getRandomMembers(int amount) {
        List<Member> members = new ArrayList<>(dm.getGuild().getMembers()); // Если перестал работать, то строчкой ниже
        //List<Member> members = new ArrayList<>(dm.getGuild().loadMembers().get());

        members.removeAll(Collections.singleton(null));

        Set<Member> randomized = new HashSet<>();
        Random r = new Random(System.currentTimeMillis());
        if (amount > members.size()) amount = members.size();

        while (randomized.size() < amount) {
            if (members.size() == 0) break;
            Member member = members.get(r.nextInt(members.size()));
            // Member member = dm.getGuild().getMember(user); может быть, тут нужно будет делать retrieve
            if (member == null || randomized.contains(member) || !member.getRoles().isEmpty() || member.getUser().isBot()) {
                members.remove(member);
                continue;
            }
            randomized.add(member);
        }
        return new ArrayList<>(randomized);
    }

}


