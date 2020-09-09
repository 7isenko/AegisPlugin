package io.github._7isenko.aegis.minecraft;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class Config {
    private final FileConfiguration config;
    private final HashMap<String, String> nomenclature;

    public Config(FileConfiguration config) {
        this.config = config;
        this.nomenclature = loadNomenclature();
    }

    public String getToken() {
        return config.getString("token");
    }

    public String getLocalizedName(String name) {
        return nomenclature.get(name);
    }

    public long getServerId() {
        return config.getLong("serverId");
    }

    public boolean isKickAllowed() {
        return config.getBoolean("allowKick");
    }

    private HashMap<String, String> loadNomenclature() {
        HashMap<String, String> n = new HashMap<>();
        n.put("main", config.getString("main"));
        n.put("control", config.getString("control"));
        n.put("whitelist", config.getString("whitelist"));

        n.put("participant", config.getString("participant"));
        n.put("picked", config.getString("picked"));

        return n;
    }
}
