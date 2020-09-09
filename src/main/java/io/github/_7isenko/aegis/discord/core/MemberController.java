package io.github._7isenko.aegis.discord.core;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.util.List;

public class MemberController extends DiscordColleague {
    private final boolean allowKick;

    public MemberController(DiscordMediator mediator) {
        super(mediator);
        this.allowKick = mediator.getConfig().isKickAllowed();
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
        if (allowKick)
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
}
