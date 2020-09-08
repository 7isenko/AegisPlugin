package io.github._7isenko.aegis.discord.core;

import net.dv8tion.jda.api.entities.Member;

import java.util.List;

public class MemberController extends DiscordColleague {
    private final boolean allowKick;

    public MemberController(DiscordMediator mediator, boolean allowKick) {
        super(mediator);
        this.allowKick = mediator.getConfig().isKickAllowed();
    }

    public void kick(Member member) {
        if (allowKick) {
            member.kick().queue();
        }
    }

    public void kickStrangers() {
        List<Member> members = mediator.getGuild().getMembers();
        for (Member member :
                members) {
            if (member.getRoles().isEmpty()) {
                member.kick().queue();
            }
        }
    }
}
