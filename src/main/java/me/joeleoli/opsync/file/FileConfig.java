package me.joeleoli.opsync.file;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class FileConfig {

    private Plugin plugin;
    private File file;
    private FileConfiguration config;

    public FileConfig(Plugin plugin, String fileName) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), fileName);

        if (!this.file.exists()) {
            this.file.getParentFile().mkdirs();

            if (plugin.getResource(fileName) == null) {
                try {
                    this.file.createNewFile();
                }
                catch (IOException e) {
                    plugin.getLogger().severe("Failed to create new file " + fileName);
                }
            }
            else {
                plugin.saveResource(fileName, false);
            }
        }

        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

}