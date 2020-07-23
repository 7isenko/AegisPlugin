package io.github._7isenko.aegis;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class DiscordCrowdRoleRemover {
    List<Member> members;
    Role role;
    Guild guild;

    public DiscordCrowdRoleRemover(List<Member> members, Role role, Guild guild) {
        this.members = members;
        this.role = role;
        this.guild = guild;
    }

    public void removeRoles() {
        for (Member m : members) {
                guild.removeRoleFromMember(m, role).queue(null, throwable -> System.out.println("When roles were removing, one guy decided to cause exception: " + throwable.getMessage()));
        }
    }
}
