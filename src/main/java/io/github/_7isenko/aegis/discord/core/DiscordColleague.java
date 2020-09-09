package io.github._7isenko.aegis.discord.core;

public abstract class DiscordColleague {
    protected DiscordMediator mediator;

    public DiscordColleague(DiscordMediator mediator) {
        this.mediator = mediator;
    }

}
