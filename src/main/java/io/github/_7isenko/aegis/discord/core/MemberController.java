package io.github._7isenko.aegis.discord.core;

import io.github._7isenko.aegis.discord.utils.MemberCollector;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.util.HashMap;
import java.util.List;

public class MemberController extends DiscordColleague {
    private final boolean allowKick;
    private final HashMap<String, MemberCollector> collectorHashMap;

    public MemberController(DiscordMediator mediator) {
        super(mediator);
        this.allowKick = mediator.getConfig().isKickAllowed();
        this.collectorHashMap = new HashMap<>();
    }

    public void kick(Member member) {
        if (allowKick) {
            try {
                member.kick().queue();
            } catch (HierarchyException e) {
                // ignore
            }
        }
    }

    public void kickStrangers() {
        if (!allowKick)
            return;
        List<Member> members = mediator.getGuild().getMembers();
        for (Member member :
                members) {
            if (member.getRoles().isEmpty()) {
                try {
                    member.kick().queue();
                } catch (HierarchyException e) {
                    // ignore
                }
            }
        }
    }

    public MemberCollector getMemberCollector(String name) {
        return collectorHashMap.get(name);
    }

    public MemberCollector createMemberCollector(String name) {
        MemberCollector collector = new MemberCollector();
        collectorHashMap.put(name, collector);
        return collector;
    }

    public void removeMemberCollector(String name) {
        collectorHashMap.remove(name);
    }
}
