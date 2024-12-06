package com.spearforge.spearannounce.modules;

import com.spearforge.spearannounce.SpearAnnounce;
import com.spearforge.spearannounce.utils.TextUtils;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class AnnounceModule {

    public static void sendAnnouncement(int index) {
        List<String> messages = SpearAnnounce.getAnnounceConfig().getStringList("Announcements." + index + ".lines");
        String permission = SpearAnnounce.getAnnounceConfig().getString("Announcements." + index + ".permission");
        List<String> worlds = SpearAnnounce.getAnnounceConfig().getStringList("Announcements." + index + ".worlds");
        String sound = SpearAnnounce.getAnnounceConfig().getString("Announcements." + index + ".sound");
        String type = SpearAnnounce.getAnnounceConfig().getString("Announcements." + index + ".type");

        for (String message : messages) {

            Bukkit.getOnlinePlayers().stream()
                    .filter(player -> playerShouldReceive(player, permission, worlds))
                    .forEach(player -> {
                        sendAnnouncementBasedOnType(message, player, type);
                        playSoundIfValid(sound, player);
                    });
        }
    }

    private static boolean playerShouldReceive(Player player, String permission, List<String> worlds) {
        return (permission == null || player.hasPermission(permission)) &&
                (worlds.isEmpty() || worlds.contains(player.getWorld().getName())) &&
                SpearAnnounce.getToggles().getOrDefault(player.getName(), true);
    }

    public static void sendAnnouncementBasedOnType(String message, Player player, String type) {
        if (SpearAnnounce.isItemsAdderActive()){
            message = FontImageWrapper.replaceFontImages(message);
        }

        if (type.equalsIgnoreCase("BOSSBAR")){
            sendBossBarAnnouncement(message, player);
        } else if (type.equalsIgnoreCase("ACTIONBAR")){
            sendActionBarAnnouncement(message, player, SpearAnnounce.getPlugin().getConfig().getInt("actionbar-time"));
        } else {
            sendBroadcastAnnouncement(message, player);
        }
    }

    private static void playSoundIfValid(String sound, Player player) {
        if (!"null".equalsIgnoreCase(sound) && isSoundValid(sound)) {
            player.playSound(player.getLocation(), Sound.valueOf(sound), 1, 1);
        }
    }

    private static boolean isSoundValid(String sound) {
        try {
            Sound.valueOf(sound);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static void sendBroadcastAnnouncement(String message, Player player) {

        message = PlaceholderAPI.setPlaceholders(player, message);
        // Check if the message has a center tag
        if (message.startsWith("<center>") && message.endsWith("</center>")) {
            message = TextUtils.centerMessage(message.substring(8, message.length() - 9));
        }
        player.sendMessage(TextUtils.formatMessage(TextUtils.processMessage(message)));

    }

    private static void sendActionBarAnnouncement(String message, Player player, int durationInSeconds) {
        String formattedMessage = PlaceholderAPI.setPlaceholders(player, message);
        new BukkitRunnable() {
            int timeLeft = durationInSeconds * 20;

            @Override
            public void run() {
                if (timeLeft <= 0) {
                    cancel();
                    return;
                }
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(TextUtils.formatMessage(TextUtils.processMessage(formattedMessage))));
                timeLeft -= 20;
            }
        }.runTaskTimer(SpearAnnounce.getPlugin(), 0, 20);
    }

    private static void sendBossBarAnnouncement(String message, Player player) {
        String formattedMessage = PlaceholderAPI.setPlaceholders(player, message);
        BossBar bossBar = Bukkit.createBossBar(
                TextUtils.formatMessage(TextUtils.processMessage(formattedMessage)),
                BarColor.valueOf(SpearAnnounce.getPlugin().getConfig().getString("bossbar-color")),
                BarStyle.valueOf(SpearAnnounce.getPlugin().getConfig().getString("bossbar-style"))
        );
        bossBar.addPlayer(player);

        new BukkitRunnable() {
            double countdown = SpearAnnounce.getPlugin().getConfig().getInt("bossbar-time");
            final double initialCountdown = countdown;

            @Override
            public void run() {
                if (countdown <= 0.0) {
                    bossBar.removeAll();
                    cancel();
                }
                bossBar.setProgress(countdown / initialCountdown);
                countdown -= 1.0;
            }
        }.runTaskTimer(SpearAnnounce.getPlugin(), 0, 20);
    }

    public static void toggleAnnouncements(String playerName) {
        SpearAnnounce.getToggles().put(playerName, !SpearAnnounce.getToggles().get(playerName));
    }

}
