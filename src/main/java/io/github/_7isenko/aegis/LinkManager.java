package io.github._7isenko.aegis;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import javax.annotation.Nullable;

public class LinkManager {
    private static LinkManager instance = null;
    private Invite invite;
    private GuildChannel channel;
    private int uses;
    private boolean active;

    public String getLink() {
        return invite.getUrl();
    }

    public void clear() {
        if (active) {
            try {
                invite.delete().queue();
                active = false;
            } catch (ErrorResponseException e) {
                // ignore
            }
        }
    }

    private Invite generate() {
        active = true;
        if (uses <= 100)
            return channel.createInvite().setUnique(true).setMaxAge(1800).setMaxUses(uses).complete();
        return channel.createInvite().setUnique(true).setMaxAge(1800).complete();
    }

    private LinkManager(GuildChannel channel, int uses) {
        this.channel = channel;
        this.uses = uses;
        invite = generate().expand().complete();
    }

    public static @Nullable
    LinkManager getInstance() {
        return instance;
    }

    public static boolean hasInstance() {
        return instance != null;
    }

    public static LinkManager setInstance(GuildChannel channel, int uses) {
        instance = new LinkManager(channel, uses);
        return instance;
    }
}
