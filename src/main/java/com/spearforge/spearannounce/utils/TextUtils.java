package com.spearforge.spearannounce.utils;

import org.bukkit.ChatColor;
import com.spearforge.spearannounce.SpearAnnounce;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {
    private static final int CHAT_WIDTH = 320;
    private static final Pattern HEX_PATTERN = Pattern.compile("#([A-Fa-f0-9]{6})");

    public static String centerMessage(String message) {
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
                continue;
            } else if (previousCode) {
                previousCode = false;
                if (c == 'l' || c == 'L') {
                    isBold = true;
                } else {
                    isBold = false;
                }
                continue;
            }

            int charWidth = getCharWidth(c, isBold);
            messagePxSize += charWidth;
        }

        int toCompensate = CHAT_WIDTH - messagePxSize;
        int spaceWidth = getCharWidth(' ', isBold);
        int compensated = toCompensate / spaceWidth / 2;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < compensated; i++) {
            sb.append(" ");
        }
        sb.append(message);

        return sb.toString();
    }

    private static int getCharWidth(char c, boolean isBold) {
        switch (c) {
            case 'i':
            case 'l':
            case 't':
            case 'I':
                return isBold ? 6 : 4;
            case ' ':
                return 4;
            case 'M':
            case 'W':
            case '@':
                return isBold ? 8 : 7;
            default:
                return isBold ? 7 : 6;
        }
    }


    public static String processMessage(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuilder result = new StringBuilder();

        int lastEnd = 0;

        while (matcher.find()) {
            String hexColor = matcher.group(1);
            String minecraftColor = convertHexToMinecraftColor(hexColor);
            result.append(message, lastEnd, matcher.start()).append(minecraftColor);
            lastEnd = matcher.end();
        }

        result.append(message.substring(lastEnd));
        return result.toString();
    }

    private static String convertHexToMinecraftColor(String hexColor) {
        StringBuilder minecraftColor = new StringBuilder("§x");
        for (char c : hexColor.toCharArray()) {
            minecraftColor.append("§").append(c);
        }
        return minecraftColor.toString();
    }

    public static String formatMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getMessageWithPrefix(String message) {
        return ChatColor.translateAlternateColorCodes('&', SpearAnnounce.getPlugin().getConfig().getString("prefix") + message);
    }

}
