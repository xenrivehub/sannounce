package com.spearforge.spearannounce;

import com.spearforge.spearannounce.commands.MainCommand;
import com.spearforge.spearannounce.configuration.CustomFileConfig;
import com.spearforge.spearannounce.listeners.PlayerJoinListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import com.spearforge.spearannounce.modules.AnnounceModule;

import java.util.HashMap;

public final class SpearAnnounce extends JavaPlugin {

    @Getter
    private static SpearAnnounce plugin;
    @Getter
    private static CustomFileConfig announceConfig;
    @Getter
    private static CustomFileConfig toggleConfig;
    @Getter
    private static HashMap<String, Boolean> toggles = new HashMap<>();
    @Getter
    private static boolean isItemsAdderActive;

    @Override
    public void onEnable() {
        isItemsAdderActive = this.getServer().getPluginManager().isPluginEnabled("ItemsAdder");
        plugin = this;
        initiliazeConfigs();
        if (toggles.isEmpty()){
            initializeToggles();
        }
        setupListeners();
        setupCommands();
        startAnnounceScheduler();

        getLogger().info("Spear Announce has been enabled!");
    }

    @Override
    public void onDisable() {
        if (!toggles.isEmpty()){
            toggles.clear();
        }
        getLogger().info("Spear Announce has been disabled!");
    }

    private void initializeToggles() {
        toggles = new HashMap<>();
        toggleConfig.createConfig();
        toggleConfig.getConfig().getKeys(false).forEach(player ->
                toggles.put(player, toggleConfig.getConfig().getBoolean(player))
        );
    }

    private void setupListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    private void setupCommands() {
        getCommand("sannounce").setExecutor(new MainCommand());
        getCommand("sannounce").setTabCompleter(new MainCommand());
    }

    private void initiliazeConfigs() {
        announceConfig = new CustomFileConfig(this, "announcements.yml");
        toggleConfig = new CustomFileConfig(this, "toggles.yml");
        announceConfig.createConfig();
        saveDefaultConfig();
    }

    private void startAnnounceScheduler() {
        int interval = getConfig().getInt("announce-interval") * 20;

        new BukkitRunnable() {
            int announcementIndex = 1;
            final int maxAnnouncements = announceConfig.getConfig().getConfigurationSection("Announcements").getKeys(false).size();

            @Override
            public void run() {
                AnnounceModule.sendAnnouncement(announcementIndex);
                if (announcementIndex == maxAnnouncements) {
                    announcementIndex = 1;
                } else {
                    announcementIndex++;
                }
            }
        }.runTaskTimer(this, 0, interval);
    }

    public void reloadPlugin() {
        announceConfig.reloadConfig();
        toggleConfig.reloadConfig();
        this.reloadConfig();
    }
}
