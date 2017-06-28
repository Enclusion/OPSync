package me.joeleoli.opsync;

import me.joeleoli.opsync.database.DatabaseCredentials;
import me.joeleoli.opsync.file.FileConfig;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

class Configuration {

    public static boolean AUTO_REFRESH = true;

    private FileConfig pluginConfig;

    Configuration(Plugin plugin) {
        this.pluginConfig = new FileConfig(plugin, "config.yml");

        this.load();
    }

    public void load() {
        FileConfiguration config = this.pluginConfig.getConfig();

        if (config.contains("settings.auto-refresh")) {
            AUTO_REFRESH = config.getBoolean("settings.auto-refresh");
        }
    }

    public void save() {

    }

    public DatabaseCredentials getDatabaseCredentials() {
        FileConfiguration config = this.pluginConfig.getConfig();

        return new DatabaseCredentials(config.getString("database.host"),
                config.getInt("database.port"),
                config.getString("database.username"),
                config.getString("database.password"),
                config.getString("database.database"));
    }

}