package io.github._7isenko.aegis.discord;

import io.github._7isenko.aegis.Aegis;
import io.github._7isenko.aegis.misc.StringsTool;
import io.github._7isenko.aegis.minecraft.UUIDHelper;
import io.github._7isenko.aegis.minecraft.WhitelistManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

public class DiscordManager {
    private static DiscordManager instance;


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
    private boolean allowKick;


    // Random
    private DiscordRandomManager discordRandomManager;

    private DiscordManager() {
        String token = Aegis.config.getString("token");
        String serverId = Aegis.config.getString("server_id");
        allowKick = Aegis.config.getBoolean("allow_kick");

            try {
                jda = BotBuilder.build(token);
            } catch (LoginException e) {
                System.out.println("токен сдох");
            }

            guild = jda.getGuildById(serverId);
            // memberRole = guild.getRoleById(memberRoleId);
            // chosenRole = guild.getRoleById(chosenRoleId);
            whitelistChannel = guild.getTextChannelById(whitelistChannelId);
            // controlChannel = guild.getTextChannelById(controlChannelId);
            // announceChannel = guild.getTextChannelById(announceChannelId);
            // greetingChannel = guild.getTextChannelById(greetingChannelId);
            discordControlMessageListener = new DiscordControlMessageListener(this);
            jda.addEventListener(discordControlMessageListener);


    }

    public void register(String username) throws IndexOutOfBoundsException, NullPointerException {
        UUID uuid = UUIDHelper.getUuid(username);
        whitelistManager.addToWhitelist(uuid);
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
        Set<Member> membersWithRoles = new HashSet<>(guild.getMembersWithRoles(memberRole));
        membersWithRoles.addAll(guild.getMembersWithRoles(chosenRole));
        membersWithRoles.forEach(member -> guild.modifyMemberRoles(member).queue(null, throwable -> controlChannel.sendMessage("Какой-то чел решил вызвать ошибку во время удаления ролей: " + throwable.getMessage()).queue()));

        // Остановка отслеживания сообщений
        try {
            jda.removeEventListener(discordEventMessageListener);
            eventActive = false;
        } catch (IllegalArgumentException e) {
            // ignore
        }
    }

    // DUPLICATE CODE
    public void setChosenRoles(int amount) {
        List<Member> members = getDiscordRandomManager().getRandomMembers(amount);
        getDiscordRandomManager().sendAnnounceMessage();
        int num = 0;
        for (Member m : members) {
            ++num;
            addRoleToMember(m, num);
        }
        announceChannel.sendMessage("Ребята, которых выбрал бот:").queue();
        StringsTool.membersToStrings(members).forEach(m -> announceChannel.sendMessage("```" + m + "```").queue());
    }

    public void addRoleToMember(Member member, int number) {
        guild.addRoleToMember(member, chosenRole).queue(aVoid -> greetingChannel.sendMessage(number + ". Приветствую, " + member.getAsMention() + ", бегом регистрироваться в <#" + whitelistChannelId + ">, и ты в съемках!").queue(),
                throwable -> controlChannel.sendMessage("Похоже, что " + member.getUser().getAsTag() + " был выбран ботом, но вышел из сервера.").queue());

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

    public boolean isAllowKick() {
        return allowKick;
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
