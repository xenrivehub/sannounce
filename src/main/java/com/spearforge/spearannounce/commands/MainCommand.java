package com.spearforge.spearannounce.commands;

import com.spearforge.spearannounce.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import com.spearforge.spearannounce.SpearAnnounce;
import com.spearforge.spearannounce.modules.AnnounceModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){
            Player p = (Player) sender;
            if (args.length == 0) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(SpearAnnounce.getPlugin().getConfig().getStringList("commands"))));
                return true;
            } else if (args[0].equalsIgnoreCase("reload")){
                if (p.hasPermission("sannounce.reload")){
                    SpearAnnounce.getPlugin().reloadPlugin();
                    p.sendMessage(TextUtils.getMessageWithPrefix(SpearAnnounce.getPlugin().getConfig().getString("reload")));
                } else {
                    p.sendMessage(TextUtils.getMessageWithPrefix(SpearAnnounce.getPlugin().getConfig().getString("no-permission")));
                }
            } else if(args[0].equalsIgnoreCase("actionbar")){
                if (p.hasPermission("sannounce.actionbar")){
                    if (args.length >= 3) {
                        StringBuilder message = new StringBuilder();
                        for (int i = 2; i < args.length; i++) {
                            message.append(args[i]).append(" ");
                        }

                        if (args[1].equalsIgnoreCase("all")) {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                AnnounceModule.sendAnnouncementBasedOnType(message.toString(), player, "ACTIONBAR");
                            }
                        } else {
                            Player target = Bukkit.getPlayer(args[1]);
                            if (target != null) {
                                AnnounceModule.sendAnnouncementBasedOnType(message.toString(), target, "ACTIONBAR");
                            } else {
                                p.sendMessage(TextUtils.getMessageWithPrefix(SpearAnnounce.getPlugin().getConfig().getString("player-not-found")));
                            }
                        }
                    } else {
                        p.sendMessage(TextUtils.getMessageWithPrefix(SpearAnnounce.getPlugin().getConfig().getString("actionbar-usage")));
                    }
                } else {
                    p.sendMessage(TextUtils.getMessageWithPrefix(SpearAnnounce.getPlugin().getConfig().getString("no-permission")));
                }
            } else if(args[0].equalsIgnoreCase("broadcast")){
                if (p.hasPermission("sannounce.broadcast")){
                    if (args.length >= 3){
                        StringBuilder message = new StringBuilder();
                        for (int i = 2; i < args.length; i++) {
                            message.append(args[i]).append(" ");
                        }

                        if (args[1].equalsIgnoreCase("all")) {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                AnnounceModule.sendAnnouncementBasedOnType(message.toString(), player, "BROADCAST");
                            }
                        } else {
                            Player target = Bukkit.getPlayer(args[1]);
                            if (target != null) {
                                AnnounceModule.sendAnnouncementBasedOnType(message.toString(), target, "BROADCAST");
                            } else {
                                p.sendMessage(TextUtils.getMessageWithPrefix(SpearAnnounce.getPlugin().getConfig().getString("player-not-found")));
                            }
                        }
                    } else {
                        p.sendMessage(TextUtils.getMessageWithPrefix(SpearAnnounce.getPlugin().getConfig().getString("broadcast-usage")));
                    }
                } else {
                    p.sendMessage(TextUtils.getMessageWithPrefix(SpearAnnounce.getPlugin().getConfig().getString("no-permission")));
                }
            } else if(args[0].equalsIgnoreCase("bossbar")){
                if (p.hasPermission("sannounce.bossbar")){
                    if (args.length >= 3){
                        StringBuilder message = new StringBuilder();
                        for (int i = 2; i < args.length; i++) {
                            message.append(args[i]).append(" ");
                        }

                        if (args[1].equalsIgnoreCase("all")) {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                AnnounceModule.sendAnnouncementBasedOnType(message.toString(), player, "BOSSBAR");
                            }
                        } else {
                            Player target = Bukkit.getPlayer(args[1]);
                            if (target != null) {
                                AnnounceModule.sendAnnouncementBasedOnType(message.toString(), target, "BOSSBAR");
                            } else {
                                p.sendMessage(TextUtils.getMessageWithPrefix(SpearAnnounce.getPlugin().getConfig().getString("player-not-found")));
                            }
                        }
                    } else {
                        p.sendMessage(TextUtils.getMessageWithPrefix(SpearAnnounce.getPlugin().getConfig().getString("bossbar-usage")));
                    }
                } else {
                    p.sendMessage(TextUtils.getMessageWithPrefix(SpearAnnounce.getPlugin().getConfig().getString("no-permission")));
                }
                // if toggle is off this will be turned off all announcements for the player even command announcements
            } else if(args[0].equalsIgnoreCase("toggle")){
                if (p.hasPermission("sannounce.toggle")){
                    AnnounceModule.toggleAnnouncements(p.getName());
                    if (SpearAnnounce.getToggles().get(p.getName())){
                        p.sendMessage(TextUtils.getMessageWithPrefix(SpearAnnounce.getPlugin().getConfig().getString("toggle-on")));
                    } else {
                        p.sendMessage(TextUtils.getMessageWithPrefix(SpearAnnounce.getPlugin().getConfig().getString("toggle-off")));
                    }
                } else {
                    p.sendMessage(TextUtils.getMessageWithPrefix(SpearAnnounce.getPlugin().getConfig().getString("no-permission")));
                }
            } else {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(SpearAnnounce.getPlugin().getConfig().getStringList("commands"))));
            }
        }



        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> firstArgs = Arrays.asList("broadcast", "actionbar", "bossbar", "toggle", "reload");
            for (String s : firstArgs) {
                if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(s);
                }
            }
        } else if (args.length == 2) {
            if ("all".toLowerCase().startsWith(args[1].toLowerCase())) {
                completions.add("all");
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                String playerName = player.getName();
                if (playerName.toLowerCase().startsWith(args[1].toLowerCase())) {
                    completions.add(playerName);
                }
            }
        }

        return completions;
    }
}
