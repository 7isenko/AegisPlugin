package io.github._7isenko.aegis;

import io.github._7isenko.aegis.minecraft.EventCommandExecutor;
import io.github._7isenko.aegis.minecraft.WhitelistManager;
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

            // Регистрация команд
            this.getCommand("event").setExecutor(new EventCommandExecutor());
            logger.info("DiscordMC is started!");
        } else {
            logger.info("You did not fill the config. Plugin is going to disable.");
            pluginManager.disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        WhitelistManager.getInstance().clearWhitelist();
        logger.info("Whitelist is clean");
    }
}