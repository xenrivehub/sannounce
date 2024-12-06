package com.spearforge.spearannounce.configuration;

import com.spearforge.spearannounce.SpearAnnounce;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CustomFileConfig {
    private final SpearAnnounce plugin;
    @Getter
    private FileConfiguration config;

    private File configFile;

    public CustomFileConfig(SpearAnnounce plugin, String fileName) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), fileName);
    }

    public void createConfig() {
        if (!this.configFile.exists()) {
            this.configFile.getParentFile().mkdirs();
            this.plugin.saveResource(this.configFile.getName(), false);
        }
        this.config = (FileConfiguration)new YamlConfiguration();
        try {
            this.config.load(this.configFile);
        } catch (IOException|org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            this.config.save(this.configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.configFile);
    }

    public void saveDefaultConfig() {
        if (!this.configFile.exists())
            this.plugin.saveResource(this.configFile.getName(), false);
    }

    public void set(String path, Object value) {
        this.config.set(path, value);
    }

    public Object get(String path) {
        return this.config.get(path);
    }

    public String getString(String path) {
        return this.config.getString(path);
    }

    public int getInt(String path) {
        return this.config.getInt(path);
    }

    public boolean getBoolean(String path) {
        return this.config.getBoolean(path);
    }

    public List<String> getStringList(String path) {
        return this.config.getStringList(path);
    }

    public void setStringList(String path, List<String> value) {
        this.config.set(path, value);
    }

    public void setInt(String path, int value) {
        this.config.set(path, Integer.valueOf(value));
    }

    public void setBoolean(String path, boolean value) {
        this.config.set(path, Boolean.valueOf(value));
    }

    public void setString(String path, String value) {
        this.config.set(path, value);
    }

    public void setDouble(String path, double value) {
        this.config.set(path, Double.valueOf(value));
    }

    public double getDouble(String path) {
        return this.config.getDouble(path);
    }

    public void setLong(String path, long value) {
        this.config.set(path, Long.valueOf(value));
    }

    public long getLong(String path) {
        return this.config.getLong(path);
    }
}
