package io.github._7isenko.aegis;

import io.github._7isenko.aegis.discord.BotBuilder;
import io.github._7isenko.aegis.discord.core.DiscordMediator;
import io.github._7isenko.aegis.minecraft.Config;
import io.github._7isenko.aegis.minecraft.EventCommandExecutor;
import io.github._7isenko.aegis.minecraft.WhitelistManager;
import io.github._7isenko.aegis.ui.CommandInvoker;
import io.github._7isenko.aegis.ui.commands.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.util.logging.Logger;

public class Aegis extends JavaPlugin {
    // How to build: Maven/Aegis/Lifecycle/package
    public static Config config;
    public static Logger logger;
    public static PluginManager pluginManager;

    @Override
    public void onEnable() {
        // Подгрузка конфига или его создание, если он отсутствует.
        this.saveDefaultConfig();
        logger = this.getLogger();
        pluginManager = Bukkit.getPluginManager();
        config = new Config(getConfig());

        if (config.getToken().equalsIgnoreCase("put_here_your_token") || config.getServerId() == 123L) {
            logger.info("Конфиг не был заполнен. Плагин выключается.");
            pluginManager.disablePlugin(this);
            return;
        }
        try {
            JDA jda = BotBuilder.build(config.getToken());
            Guild guild = jda.getGuildById(config.getServerId());

            DiscordMediator mediator = new DiscordMediator(jda, guild, config);
            CommandInvoker invoker = fillInvoker(mediator);

            this.getCommand("event").setExecutor(new EventCommandExecutor(invoker));
        } catch (LoginException e) {
            logger.info("Токен бота недействителен.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("Aegis запущен.");
    }

    @Override
    public void onDisable() {
        CommandInvoker.getInstance().execute("stop", (String[]) null);
        logger.info("Вайтлист очищен");
    }

    private CommandInvoker fillInvoker(DiscordMediator dm) {
        CommandInvoker invoker = CommandInvoker.getInstance();
        invoker.addCommand("add", new AddCommand(dm));
        invoker.addCommand("start", new StartCommand(dm));
        invoker.addCommand("stop", new StopCommand(dm));
        invoker.addCommand("set", new SetCommand());
        invoker.addCommand("kick", new KickCommand(dm));
        invoker.addCommand("help", new HelpCommand());
        return invoker;
    }
}