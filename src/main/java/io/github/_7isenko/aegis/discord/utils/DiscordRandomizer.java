package io.github._7isenko.aegis.discord.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.*;

public class DiscordRandomizer {
    public static Set<Member> getRandomMembers(Guild guild, int amount, Collection<Role> excluded) {
        List<Member> members = new ArrayList<>(guild.getMembers());

        Set<Member> randomized = new HashSet<>();
        Random r = new Random(System.currentTimeMillis());
        if (amount > members.size()) amount = members.size();

        while (randomized.size() < amount) {
            if (members.size() == 0) break;
            Member member = members.get(r.nextInt(members.size()));
            if (member == null || randomized.contains(member) || hasRole(member, excluded) || member.getUser().isBot()) {
                members.remove(member);
                continue;
            }
            randomized.add(member);
        }
        return randomized;
    }

    private static boolean hasRole(Member member, Collection<Role> roles) {
        if (roles == null)
            return !member.getRoles().isEmpty();
        for (Role role : roles) {
            if (member.getRoles().contains(role))
                return true;
        }
        return false;
    }

}


