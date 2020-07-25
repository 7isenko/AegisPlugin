package io.github._7isenko.aegis;

import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FakeAddDiscordManager {
    private static FakeAddDiscordManager instance;
    private List<Member> members = Collections.emptyList();
    private int lastAmount = 0;
    private DiscordManager dm = DiscordManager.getInstance();

    public void getChosenRoles() {
        members = dm.getDiscordEmoteManager().getEmotedMembers(lastAmount);
    }

    public void getChosenRoles(int amount) {
        lastAmount = amount;
        members = dm.getDiscordEmoteManager().getEmotedMembers(amount);
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
        ArrayList<String> strings = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Member member : members) {
            i++;
            sb.append(i).append(". ").append(member.getUser().getAsTag()).append("\n");
            if (i % 20 == 0) {
                strings.add(sb.toString());
                sb = new StringBuilder();
            }
        }
        strings.add(sb.toString());
        return strings;
    }

    public void setChosenRoles() {
        int num = 0;
        for (Member m : members) {
            ++num;
            dm.addRoleToMember(m, num);
        }
    }

    public void clear() {
        instance = null;
    }
}
