package io.github._7isenko.aegis;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class UUIDRegisterListener implements Listener {
    private static ArrayList<OfflinePlayer> whitelistedPlayers = new ArrayList<>();

    @EventHandler
    public void onPlayerRegister(UUIDRegisterEvent event) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(event.getUUID());
        if (!player.isWhitelisted()) {
            // Те, кто уже в вайтлисте, удаляться после команды stop не будут.
            player.setWhitelisted(true);
            whitelistedPlayers.add(player);
        }
    }

    public static void clearWhitelist() {
        for (OfflinePlayer p :
                whitelistedPlayers) {
            p.setWhitelisted(false);
        }
        whitelistedPlayers = new ArrayList<>();
    }
}
