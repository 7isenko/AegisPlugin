package io.github._7isenko.aegis;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.*;

public class DiscordEmoteManager {
    private final DiscordManager dm;
    private final TextChannel announceChannel;
    private Message emoteMessage;
    private String[] messages = {"*ввввд-жуужу-жббжжужу*\nРобот запущен. Давайте пятюню!",
            "Чтооо? Опять? Ну ладно, жмите пятюню...",
            "Сегодня так много новых лиц! \n" + "Рад всех видеть, давайте пять!", "Срочные новости!!! Робот хочет пятюню!",
            "- Я всего лишь хочу тебе объяснить, что все люди созданы разными, с разными возможностями и разными потребностями. Глупо было бы требовать от всех людей одного и того же, это всё равно что заставлять пингвина летать или рыбу петь...\n" +
                    "- Петрович, заебал, нажми уже пятюню!",
            "Волк волку враг, если не жмёт пять! АУФ",
            "ожидаю ваши пятюни, пока пью энергетосик. ммм адреналинчик...",
            "Чтобы ПЯТЮНЯ была у меня на столе через ПЯТЬ секунд!"};

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
        List<Member> members = new ArrayList<>(dm.getGuild().getMembers());
        members.removeAll(Collections.singleton(null));
        dm.getAnnounceChannel().retrieveReactionUsersById(emoteMessage.getId(), "✋").complete();

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


