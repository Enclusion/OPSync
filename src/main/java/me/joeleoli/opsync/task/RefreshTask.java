package me.joeleoli.opsync.task;

import me.joeleoli.opsync.OPSync;
import me.joeleoli.opsync.data.DataCache;
import me.joeleoli.opsync.data.GenericCallback;

import org.bukkit.scheduler.BukkitRunnable;

public class RefreshTask extends BukkitRunnable {

    public RefreshTask() {
        this.runTaskTimer(OPSync.getInstance(), 20L * 7, 20L * 30);
    }

    public void run() {
        OPSync.getInstance().getDatabaseManager().refreshAuthorizedCache(new GenericCallback() {
            @Override
            public void call(boolean result, boolean error) {
                if (error) {
                    OPSync.getInstance().getLogger().severe("Failed to refresh authorized users!");
                    return;
                }

                OPSync.getInstance().getLogger().info("Refreshed authorized users!");
            }
        });

        DataCache.getInstance().refresh();
    }

}