package io.github._7isenko.aegis.utils;

import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StringsTool {
    public static List<String> membersToTags(Collection<Member> members) {
        ArrayList<String> strings = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Member member : members) {
            i++;
            sb.append(i).append(". ");
            if (member != null)
                sb.append(member.getUser().getAsTag()).append("\n");
            else sb.append("dummy#0000").append("\n");
            if (i % 20 == 0) {
                strings.add(sb.toString());
                sb = new StringBuilder();
            }
        }
        if (sb.length() != 0)
            strings.add(sb.toString());
        return strings;
    }

    public static List<String> membersToMentions(Collection<Member> members) {
        ArrayList<String> strings = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Member member : members) {
            i++;
            sb.append(i).append(". ");
            if (member != null)
                sb.append(member.getUser().getAsMention()).append("\n");
            else sb.append("dummy#0000").append("\n");
            if (i % 20 == 0) {
                strings.add(sb.toString());
                sb = new StringBuilder();
            }
        }
        if (sb.length() != 0)
            strings.add(sb.toString());
        return strings;
    }
}
