package io.github._7isenko.aegis.discord.core;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class RoleController extends DiscordColleague {

    private final Role participantRole;
    private final Role pickedRole;

    public RoleController(DiscordMediator mediator) {
        super(mediator);
        this.participantRole = getRole(mediator.getConfig().getLocalizedName("participant"));
        this.pickedRole = getRole(mediator.getConfig().getLocalizedName("picked"));
    }

    public void clearPartakers() {
        List<Member> partakers = getParticipants();
        partakers.addAll(getPicked());
        partakers.forEach(p -> p.getGuild().modifyMemberRoles(p, null, Arrays.asList(participantRole, pickedRole)).queue());
    }

    public List<Member> getParticipants() {
        return mediator.getGuild().getMembersWithRoles(participantRole);
    }

    public List<Member> getPicked() {
        return mediator.getGuild().getMembersWithRoles(pickedRole);
    }

    public void addParticipantRole(Member member) {
        this.addRole(member, participantRole);
    }

    public void addPickedRole(Member member) {
        this.addRole(member, pickedRole);
    }

    public void addRole(Member member, Role role) {
        mediator.getGuild().addRoleToMember(member, role).queue(null,
                throwable -> mediator.getLogger().info("Похоже, что " + member.getUser().getAsTag() + " вышел из сервера при выдаче роли."));
    }

    public void removeRole(Member member, Role role) {
        mediator.getGuild().removeRoleFromMember(member, role).queue();
    }

    public Role getParticipantRole() {
        return participantRole;
    }

    public Role getPickedRole() {
        return pickedRole;
    }

    public Role getRole(String name) {
        return searchRole(name);
    }

    @NotNull
    public Role searchRole(@NotNull List<String> names) {
        if (names.isEmpty()) throw new IllegalArgumentException("Empty list of names");
        for (String name : names) {
            List<Role> roles = mediator.getGuild().getRolesByName(name, true);
            if (!roles.isEmpty()) {
                return roles.get(0);
            }
        }
        return createRole(names.get(0));
    }

    @NotNull
    public Role searchRole(@NotNull String name) {
        List<Role> roles = mediator.getGuild().getRolesByName(name, true);
        if (!roles.isEmpty()) {
            return roles.get(0);
        }
        return createRole(name);
    }

    @NotNull
    public Role createRole(@NotNull String name) {
        return mediator.getGuild().createRole().setName(name).complete();
    }

}
