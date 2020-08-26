package io.github._7isenko.aegis;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class WhitelistManager {
    private HashSet<OfflinePlayer> whitelistedPlayers;
    private static WhitelistManager instance;

    private WhitelistManager() {
        whitelistedPlayers = new HashSet<>();
    }

    public void addToWhitelist(UUID uuid) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        if (!player.isWhitelisted()) {
            // Те, кто уже в вайтлисте, удаляться после команды stop не будут.
            player.setWhitelisted(true);
            whitelistedPlayers.add(player);
        }
    }

    public void clearWhitelist() {
        for (OfflinePlayer p :
                whitelistedPlayers) {
            p.setWhitelisted(false);
        }
        whitelistedPlayers = new HashSet<>();
    }

    public static WhitelistManager getInstance() {
        if (instance == null) {
            instance = new WhitelistManager();
        }
        return instance;
    }

    public static boolean hasInstance() {
        return instance != null;
    }
}
