package io.github._7isenko.aegis.discord.utils;

import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class MemberCollector {
    private final LinkedHashSet<Member> memberList;

    public MemberCollector() {
        this.memberList = new LinkedHashSet<>();
    }

    public void addMember(Member member) {
        memberList.add(member);
    }

    public List<Member> getMembers() {
        return new ArrayList<>(memberList);
    }
}
