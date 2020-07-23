package io.github._7isenko.aegis;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public class DiscordEmoteManager {
    private final DiscordManager dm;
    private final TextChannel announceChannel;
    private Message emoteMessage;
    private String[] messages = {"*ввввд-жуужу-жббжжужу*\nРобот запущен. Давайте пятюню!", "Чтооо? Опять? Ну ладно, жмите пятюню...", "Сегодня так много новых лиц! \n" +
            "Рад всех видеть, давайте пять!", "Срочные новости!!! Робот хочет пятюню!", "- Я всего лишь хочу тебе объяснить, что все люди созданы разными, с разными возможностями и разными потребностями. Глупо было бы требовать от всех людей одного и того же, это всё равно что заставлять пингвина летать или рыбу петь...\n" +
            "- Петрович, заебал, нажми уже пятюню!", "Волк волку враг, если не жмёт пять! АУФ", "ожидаю ваши пятюни, пока пью энергетосик. ммм адреналинчик...", "Чтобы ПЯТЮНЯ была у меня на столе через ПЯТЬ секунд!"};

    public DiscordEmoteManager(DiscordManager dm) {
        this.dm = dm;
        this.announceChannel = dm.getAnnounceChannel();
        sendEmoteMessage();
    }

    private String generateCoolMessage() {
        return messages[new Random().nextInt(messages.length)];
    }

    private void sendEmoteMessage() {
        announceChannel.sendMessage(generateCoolMessage()).queue(message -> {
            message.addReaction("✋").queue();
            emoteMessage = message;
        });
    }

    public List<Member> getEmotedMembers(int amount) {
        List<User> users = dm.getAnnounceChannel().retrieveReactionUsersById(emoteMessage.getId(), "✋").complete();
        Set<Member> randomized = new HashSet<>();
        Random r;

        try {
            r = SecureRandom.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            r = new Random();
        }

        if (amount > users.size()) amount = users.size();

        while (randomized.size() < amount) {
            if (users.size() == 0) break;
            User user = users.get(r.nextInt(users.size()));
            Member member = dm.getGuild().getMember(user);
            if (member == null || randomized.contains(member) || member.getRoles().contains(dm.getChosenRole()) || member.getRoles().contains(dm.getMemberRole()) || member.getUser().isBot()) {
                users.remove(user);
                continue;
            }
            randomized.add(member);
        }
        return new ArrayList<>(randomized);
    }

    private List<String> getTags(List<User> users) {
        ArrayList<String> tags = new ArrayList<>();
        for (User user : users) {
            try {
                tags.add(user.getAsTag());
            } catch (Exception e) {
                tags.add("ливнул");
            }
        }
        return tags;
    }
}


