package io.github._7isenko.aegis;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.*;

public class DiscordRandomManager {
    private final DiscordManager dm;
    private final TextChannel announceChannel;
    private String[] messages = {"*ввввд-жуужу-жббжжужу*\nРобот запущен. Рандом вот-вот начнется!",
            "Чтооо? Опять? Ну ладно, ждите, пока я разогреюсь...",
            "Сегодня так много новых лиц! \n" + "Рад всех видеть, надеюсь, будет много новичков!",
            "Срочные новости!!! Робот хочет тяночку! \nОй, что? Этой фразы тут не должно быть...",
            "- Я всего лишь хочу тебе объяснить, что все люди созданы разными, с разными возможностями и разными потребностями. Глупо было бы требовать от всех людей одного и того же, это всё равно что заставлять пингвина летать или рыбу петь...\n" +
                    "- Петрович, заебал, начинай уже рандомить!!!",
            "кроссаут",
            "Бот. Джеймс Бот.",
            "привет фывфыв я в дс но не на серве добавишь в вайт лист?? я ждал 68 часов и не успел го перенабор пожалуйста!!! почему я в канале съемки но не слышу фыва что мне делать??? эм я зашел на серв , но не в дс закинь в гч съемки пожалуйста!! привет фывфыв добавь в друзья вк прошууу . закрепи этот комментарий если любишь подписчиков. эмммм привет фывфыв я упал в самом начале го второй шанс пж..."};

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
        List<Member> members = new ArrayList<>(dm.getGuild().getMembers());
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


