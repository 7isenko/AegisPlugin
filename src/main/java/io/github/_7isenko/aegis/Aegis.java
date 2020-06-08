package io.github._7isenko.aegis;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Aegis extends JavaPlugin {
    // How to build: Maven/Aegis/Lifecycle/package
    public static FileConfiguration config;
    public static Logger logger;
    public static PluginManager pluginManager;

    @Override
    public void onEnable() {
        // Подгрузка конфига или его создание, если он отсутствует.
        this.saveDefaultConfig();
        config = this.getConfig();
        logger = this.getLogger();
        pluginManager = Bukkit.getPluginManager();

        if (config.getBoolean("filled")) {
            DiscordManager.getInstance();

            // Регистрация команд
            this.getCommand("event").setExecutor(new EventCommand());
            logger.info("DiscordMC is started!");
        } else {
            logger.info("You did not fill the config. Plugin is going to disable.");
            pluginManager.disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (WhitelistManager.hasInstance()) {
            WhitelistManager.getInstance().clearWhitelist();
            logger.info("Whitelist is clean");
        }
        if (DiscordManager.hasInstance()) {
            DiscordManager.getInstance().disableDiscordListener();
            DiscordManager.getInstance().stop();
            logger.info("Members are gone");
            logger.info("Discord listener is off");
        }
    }
}