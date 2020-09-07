package io.github._7isenko.aegis.discord.controllers;

import net.dv8tion.jda.api.JDA;

public abstract class AbstractController {
    private JDA jda;

    public AbstractController(JDA jda) {
        this.jda = jda;
    }

}
