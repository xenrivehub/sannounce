package com.spearforge.spearannounce.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import com.spearforge.spearannounce.SpearAnnounce;
import com.spearforge.spearannounce.utils.TextUtils;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinListener implements Listener {


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        boolean isTrue = SpearAnnounce.getPlugin().getConfig().getBoolean("join-message");
        String joinMessage = SpearAnnounce.getPlugin().getConfig().getString("join-message-text");
        String firstJoinMessage = SpearAnnounce.getPlugin().getConfig().getString("first-join-message-text");
        int playerCount = Bukkit.getOfflinePlayers().length-1;

        // if join message is true, send the message
        if (isTrue){
           if (e.getPlayer().hasPlayedBefore()){
               e.setJoinMessage(TextUtils.formatMessage(TextUtils.processMessage(joinMessage.replaceAll("%player%", e.getPlayer().getName()).replaceAll("%playercount%", Integer.toString(playerCount)))));
           } else {
                e.setJoinMessage(TextUtils.formatMessage(TextUtils.processMessage(firstJoinMessage.replaceAll("%player%", e.getPlayer().getName()).replaceAll("%playercount%", Integer.toString(playerCount)))));
           }
        }

        // setting the player's toggle to true if it doesn't exist
        if (!SpearAnnounce.getToggleConfig().getConfig().getBoolean(e.getPlayer().getName())){
            SpearAnnounce.getToggleConfig().set(e.getPlayer().getName(), true);
        }
        if (!SpearAnnounce.getToggles().containsKey(e.getPlayer().getName())){
            SpearAnnounce.getToggles().put(e.getPlayer().getName(), true);
        }
        SpearAnnounce.getToggleConfig().saveConfig();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        SpearAnnounce.getToggleConfig().set(e.getPlayer().getName(), SpearAnnounce.getToggles().get(e.getPlayer().getName()));
        SpearAnnounce.getToggleConfig().saveConfig();
    }

}
