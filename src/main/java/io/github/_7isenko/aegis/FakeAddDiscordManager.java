package io.github._7isenko.aegis;

import net.dv8tion.jda.api.entities.Member;

import java.util.Collections;
import java.util.List;

public class FakeAddDiscordManager {
    private static FakeAddDiscordManager instance;
    private List<Member> members = Collections.emptyList();
    private int lastAmount = 0;
    private DiscordManager dm = DiscordManager.getInstance();

    public void getChosenRoles() {
        members = dm.getDiscordRandomManager().getRandomMembers(lastAmount);
    }

    public void getChosenRoles(int amount) {
        lastAmount = amount;
        members = dm.getDiscordRandomManager().getRandomMembers(amount);
    }


    private FakeAddDiscordManager() {
    }

    public static FakeAddDiscordManager getInstance() {
        if (instance == null) {
            instance = new FakeAddDiscordManager();
        }
        return instance;
    }

    public boolean isChecked() {
        return members == null || members.isEmpty();
    }

    public static boolean hasInstance() {
        return instance != null;
    }


    public List<String> getChosen() {
        return StringsTool.membersToStrings(members);
    }

    // DUPLICATE CODE
    public void setChosenRoles() {
        dm.getDiscordRandomManager().sendAnnounceMessage();
        int num = 0;
        for (Member m : members) {
            ++num;
            dm.addRoleToMember(m, num);
        }
        dm.getAnnounceChannel().sendMessage("Ребята, которых выбрал бот:").queue();
        StringsTool.membersToStrings(members).forEach(m -> dm.getAnnounceChannel().sendMessage("```" + m + "```").queue());

    }

    public void clear() {
        instance = null;
    }
}
