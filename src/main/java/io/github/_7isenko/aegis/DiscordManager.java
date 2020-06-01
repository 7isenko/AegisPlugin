package io.github._7isenko.aegis;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;


import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DiscordManager {
    private static DiscordManager instance;
    private Logger logger;

    private String token;
    private String serverId;
    private String roleId;
    private String channelId;

    private JDABuilder builder;
    private JDA jda;

    private Guild guild;
    private Role role;
    private TextChannel channel;

    private DiscordMessageListener discordMessageListener;

    private DiscordManager() {
        token = Aegis.config.getString("token");
        serverId = Aegis.config.getString("server_id");
        roleId = Aegis.config.getString("member_role_id");
        channelId = Aegis.config.getString("whitelist_channel_id");
        logger = Aegis.logger;

        // Config and build the JDA
        builder = JDABuilder.createDefault(token);
        configureBuilder();
        try {
            jda = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
            logger.info("Token problems. Contact admin");
        }

        // Filling data
        try {
            jda.awaitReady();
            guild = jda.getGuildById(serverId);
            role = guild.getRoleById(roleId);
            channel = guild.getTextChannelById(channelId);

        } catch (InterruptedException e) {
            // ignore
        }

    }

    private void configureBuilder() {
        builder.setActivity(Activity.listening("чат"));
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
    }

    public void turnOffChannel() {
        // TODO: override TextChannel perms
        // channel.getPermissionOverride(guild.getPublicRole()).delete().queue();
        // channel.createPermissionOverride(guild.getPublicRole()).setDeny(Permission.MESSAGE_WRITE).queue();
    }

    public void turnOnChannel() {
        // TODO: override TextChannel perms
        // channel.getPermissionOverride(guild.getPublicRole()).delete().queue();
        // channel.createPermissionOverride(guild.getPublicRole()).setAllow(Permission.MESSAGE_WRITE).queue();
    }

    public void turnOnListener() {
        discordMessageListener = new DiscordMessageListener();
        jda.addEventListener(discordMessageListener);
    }

    public void turnOffListener() {
        List<Member> membersWithRoles = guild.getMembersWithRoles(role);
        for (Member m:
             membersWithRoles) {
            guild.removeRoleFromMember(m, role).complete();
        }
        try {
            jda.removeEventListener(discordMessageListener);
        } catch (IllegalArgumentException e) {
            // ignore
        }
    }

    public static DiscordManager getInstance() {
        if (instance == null) {
            instance = new DiscordManager();
        }
        return instance;
    }

    public static boolean hasInstance() {
        return instance != null;
    }
}
