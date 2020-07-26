package io.github._7isenko.aegis;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class DiscordManager {
    private static DiscordManager instance;
    private Logger logger;

    private JDABuilder builder;
    private JDA jda;

    private Guild guild;
    private Role memberRole;
    private Role chosenRole;
    private TextChannel whitelistChannel;
    private TextChannel controlChannel;
    private TextChannel announceChannel;
    private TextChannel greetingChannel;

    private boolean eventActive;

    private DiscordEventMessageListener discordEventMessageListener;
    private DiscordControlMessageListener discordControlMessageListener;
    private WhitelistManager whitelistManager;
    private String whitelistChannelId;

    // Random
    private DiscordRandomManager discordRandomManager;

    private DiscordManager() {
        String token = Aegis.config.getString("token");
        String serverId = Aegis.config.getString("server_id");
        String memberRoleId = Aegis.config.getString("member_role_id");
        String chosenRoleId = Aegis.config.getString("chosen_role_id");
        whitelistChannelId = Aegis.config.getString("whitelist_channel_id");
        String controlChannelId = Aegis.config.getString("control_channel_id");
        String announceChannelId = Aegis.config.getString("announce_channel_id");
        String greetingChannelId = Aegis.config.getString("greeting_channel_id");
        logger = Aegis.logger;
        eventActive = false;

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
            memberRole = guild.getRoleById(memberRoleId);
            chosenRole = guild.getRoleById(chosenRoleId);
            whitelistChannel = guild.getTextChannelById(whitelistChannelId);
            controlChannel = guild.getTextChannelById(controlChannelId);
            announceChannel = guild.getTextChannelById(announceChannelId);
            greetingChannel = guild.getTextChannelById(greetingChannelId);
            discordControlMessageListener = new DiscordControlMessageListener(this);
            jda.addEventListener(discordControlMessageListener);
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
        builder.setRelativeRateLimit(true);
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
        if (eventActive) return;

        // Запуск менеджера вайтлиста
        whitelistManager = WhitelistManager.getInstance();

        // Запуск отслеживания сообщений в канале "whitelist_channel"
        discordEventMessageListener = new DiscordEventMessageListener(this);
        jda.addEventListener(discordEventMessageListener);
        eventActive = true;
    }

    public void stop() {
        // Чистка вайтлиста
        if (whitelistManager != null) {
            whitelistManager.clearWhitelist();
        }

        // Удаление разданных ролей
        new DiscordRoleRemover(guild.getMembersWithRoles(memberRole), memberRole, guild).removeRoles();

        // Удаление разданных ролей 2
        stopRandomMode();

        // Остановка отслеживания сообщений
        try {
            jda.removeEventListener(discordEventMessageListener);
            eventActive = false;
        } catch (IllegalArgumentException e) {
            // ignore
        }
    }

    public void setChosenRoles(int amount) {
        List<Member> members = getDiscordRandomManager().getRandomMembers(amount);
        getDiscordRandomManager().sendAnnounceMessage();
        int num = 0;
        for (Member m : members) {
            ++num;
            addRoleToMember(m, num);
        }
    }

    public void addRoleToMember(Member member, int number) {
        guild.addRoleToMember(member, chosenRole).queue(aVoid -> greetingChannel.sendMessage(number + ". Приветствую, " + member.getAsMention() + ", бегом регистрироваться в <#" + whitelistChannelId + ">, и ты в съемках!").queue(),
                throwable -> controlChannel.sendMessage("Похоже, что " + member.getUser().getAsTag() + " был выбран ботом, но вышел из сервера.").queue());

    }

    public void stopRandomMode() {
        new DiscordRoleRemover(guild.getMembersWithRoles(chosenRole), chosenRole, guild).removeRoles();
    }

    public static DiscordManager getInstance() {
        if (instance == null) {
            instance = new DiscordManager();
        }
        return instance;
    }

    public DiscordRandomManager getDiscordRandomManager() {
        if (discordRandomManager == null)
            discordRandomManager = new DiscordRandomManager(this);
        return discordRandomManager;
    }

    public static boolean hasInstance() {
        return instance != null;
    }

    public Guild getGuild() {
        return guild;
    }

    public Role getMemberRole() {
        return memberRole;
    }

    public TextChannel getWhitelistChannel() {
        return whitelistChannel;
    }

    public Role getChosenRole() {
        return chosenRole;
    }

    public TextChannel getControlChannel() {
        return controlChannel;
    }

    public TextChannel getAnnounceChannel() {
        return announceChannel;
    }

    public boolean isStarted() {
        return eventActive;
    }

    public void disableDiscordListener() {
        try {
            jda.removeEventListener(discordControlMessageListener);
        } catch (Exception e) {
            // ignore
        }

    }

    public TextChannel getGreetingChannel() {
        return greetingChannel;
    }
}
