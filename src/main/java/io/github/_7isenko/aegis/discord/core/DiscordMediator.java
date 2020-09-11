package io.github._7isenko.aegis.discord.core;

import io.github._7isenko.aegis.discord.DiscordMessageListener;
import io.github._7isenko.aegis.minecraft.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

public class DiscordMediator {
    private final JDA jda;
    private final Guild guild;
    private final Config config;
    private final ChatController chatController;
    private final RoleController roleController;
    private final MemberController memberController;
    private final DiscordLogger logger;
    private final ListenerController listenerController;

    public DiscordMediator(JDA jda, Guild guild, Config config) {
        this.jda = jda;
        this.guild = guild;
        this.config = config;
        this.roleController = new RoleController(this);
        this.chatController = new ChatController(this);
        this.memberController = new MemberController(this);
        this.logger = new DiscordLogger(this);
        this.listenerController = new ListenerController(this);
    }

    public JDA getJda() {
        return jda;
    }

    public Guild getGuild() {
        return guild;
    }

    public RoleController getRoleController() {
        return roleController;
    }

    public ChatController getChatController() {
        return chatController;
    }

    public DiscordLogger getLogger() {
        return logger;
    }

    public MemberController getMemberController() {
        return memberController;
    }

    public Config getConfig() {
        return config;
    }

    public DiscordMessageListener getMessageListener() {
        return listenerController.getDiscordMessageListener();
    }

    public ListenerController getListenerController() {
        return listenerController;
    }
}
