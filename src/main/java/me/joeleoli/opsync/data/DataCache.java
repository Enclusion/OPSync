package me.joeleoli.opsync.data;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataCache {

    private static DataCache instance;

    private List<UUID> authorized = new ArrayList<>();

    public DataCache() {
        instance = this;
    }

    public void addAuthorized(UUID uuid) {
        if (!this.authorized.contains(uuid)) {
            this.authorized.add(uuid);
        }
    }

    public void removeAuthorized(UUID uuid) {
        if (this.authorized.contains(uuid)) {
            this.authorized.remove(uuid);
        }
    }

    public void setAuthorized(List<UUID> authorized) {
        this.authorized = authorized;
    }

    public void refresh() {
        for (OfflinePlayer player : Bukkit.getServer().getOperators()) {
            if (!this.authorized.contains(player.getUniqueId())) {
                player.setOp(false);

                if (player.isOnline()) {
                    Player p = Bukkit.getServer().getPlayer(player.getUniqueId());
                    p.setOp(false);
                }
            }
        }

        for (UUID uuid : this.authorized) {
            OfflinePlayer offline = Bukkit.getServer().getOfflinePlayer(uuid);

            if (offline != null) {
                offline.setOp(true);

                if (offline.isOnline()) {
                    Player player = Bukkit.getServer().getPlayer(uuid);
                    player.setOp(true);
                }
            }
        }
    }

    public static DataCache getInstance() {
        return instance;
    }

}