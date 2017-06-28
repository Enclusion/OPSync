package me.joeleoli.opsync;

import me.joeleoli.opsync.command.OPSyncCommand;
import me.joeleoli.opsync.data.DataCache;
import me.joeleoli.opsync.data.GenericCallback;
import me.joeleoli.opsync.database.DatabaseManager;
import me.joeleoli.opsync.task.RefreshTask;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/*
    Sync operators and remove unauthorized operators across multiple servers.
 */
public class OPSync extends JavaPlugin {

    private static OPSync instance;

    private Configuration configuration;
    private DatabaseManager databaseManager;

    public void onEnable() {
        instance = this;

        this.configuration = new Configuration(this);
        this.databaseManager = new DatabaseManager(this, this.configuration.getDatabaseCredentials());

        new DataCache();

        this.registerCommands();
        this.startupTask();
    }

    private void registerCommands() {
        this.getCommand("opsync").setExecutor(new OPSyncCommand(this));
    }

    private void startupTask() {
        new BukkitRunnable() {
            public void run() {
                OPSync.this.databaseManager.generateTables();
            }
        }.runTaskLater(this, 20L * 3);

        new BukkitRunnable() {
            public void run() {
                OPSync.this.databaseManager.refreshAuthorizedCache(new GenericCallback() {
                    @Override
                    public void call(boolean result, boolean error) {
                        if (error) {
                            OPSync.this.getLogger().severe("The server failed to establish a connection to the database.");
                        }

                        if (result) {
                            OPSync.this.getLogger().info("Loaded authorization list!");
                        }
                    }
                });
            }
        }.runTaskLater(this, 20L * 7);

        if (Configuration.AUTO_REFRESH) {
            new RefreshTask();
        }
    }

    public static OPSync getInstance() {
        return instance;
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

}