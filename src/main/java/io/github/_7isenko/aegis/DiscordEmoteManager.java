package io.github._7isenko.aegis;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
        List<Member> members = new ArrayList<>();
        int[] i = {0};
        List<User> users = dm.getAnnounceChannel().retrieveReactionUsersById(emoteMessage.getId(), "✋").complete();
        StatsCollector.getInstance().setBeforeShuffle(users);
        Collections.shuffle(users);
        StatsCollector.getInstance().setAfterShuffle(users);
        users.forEach((user) -> {
            try {
                Member member = dm.getGuild().getMember(user);
                if (member == null) {
                    dm.getControlChannel().sendMessage("Очередной клоун на " + user.getAsTag() + " решил ливнуть во время сбора людей.").queue();
                    return;
                }
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


