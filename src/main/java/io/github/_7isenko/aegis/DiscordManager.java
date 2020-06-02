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

    private boolean active;

    private DiscordMessageListener discordMessageListener;

    private DiscordManager() {
        token = Aegis.config.getString("token");
        serverId = Aegis.config.getString("server_id");
        roleId = Aegis.config.getString("member_role_id");
        channelId = Aegis.config.getString("whitelist_channel_id");
        logger = Aegis.logger;
        active = false;

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

    public void turnOnListener() {
        if (active) return;
        discordMessageListener = new DiscordMessageListener();
        jda.addEventListener(discordMessageListener);
        active = true;
    }

    public void turnOffListener() {
        // FIXME: исправить то, что нужно писть команду 10 раз. сделать асинхронный поток
        List<Member> membersWithRoles = guild.getMembersWithRoles(role);
        for (Member m :
                membersWithRoles) {
            try {
                guild.removeRoleFromMember(m, role).queue();
            } catch (Exception e) {
                logger.info("Исключение в очистке ролей!!! " + e.getMessage());
            }
        }

        try {
            jda.removeEventListener(discordMessageListener);
            active = false;
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

    public void kickWithoutRoles() {
        List<Member> members = guild.getMembers();
        for (Member member :
                members) {
            if (member.getRoles().isEmpty()) {
                member.kick().queue();
            }
        }
    }
}
