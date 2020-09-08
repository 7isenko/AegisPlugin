package io.github._7isenko.aegis.minecraft;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    private final FileConfiguration config;

    public Config(FileConfiguration config) {
        this.config = config;
    }

    public boolean isKickAllowed() {
        return config.getBoolean("allowKick");
    }
}
