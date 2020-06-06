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
import java.util.UUID;
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
    private WhitelistManager whitelistManager;

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

    public void register(String username) throws IndexOutOfBoundsException, NullPointerException {
        UUID uuid = UUIDGetter.getInstance().getUuid(username);
        whitelistManager.addToWhitelist(uuid);
    }

    private void configureBuilder() {
        builder.setActivity(Activity.listening("чат"));
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
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

    public void start() {
        // Не допускаем двукратный старт
        if (active) return;

        // Запуск менеджера вайтлиста
        whitelistManager = WhitelistManager.getInstance();

        // Запуск отслеживания сообщений в канале "whitelist_channel"
        discordMessageListener = new DiscordMessageListener(this);
        jda.addEventListener(discordMessageListener);
        active = true;
    }

    public void stop() {
        // Чистка вайтлиста
        if (whitelistManager != null) {
            whitelistManager.clearWhitelist();
        }

        // Удаление разданных ролей
        List<Member> membersWithRoles = guild.getMembersWithRoles(role);
        for (Member m : membersWithRoles) {
            try {
                guild.removeRoleFromMember(m, role).queue();
            } catch (Exception e) {
                logger.info("Проблема при удалении роли: " + e.getMessage());
            }
        }

        // Остановка отслеживания сообщений
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

}
