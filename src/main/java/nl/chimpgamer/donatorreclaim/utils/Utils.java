package nl.chimpgamer.donatorreclaim.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final Pattern HEX_PATTERN = Pattern.compile("&#(?:[0-9a-fA-F]{6})");
    private static final Pattern UUID_PATTERN = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}");

    public static String capitalize(String string) {
        List<String> list = new ArrayList<>();
        String[] strings = string.split("[ _]");
        for (String string1 : strings) {
            list.add(string1.substring(0, 1).toUpperCase() + string1.substring(1).toLowerCase());
        }
        return String.join(" ", list);
    }

    public static String formatColorCodes(String input) {
        return ChatColor.translateAlternateColorCodes('&', formatHexColors(input));
    }

    private static String formatHexColors(String input) {
        try {
            String result = input;
            ChatColor.class.getMethod("of", String.class);
            Matcher matcher = HEX_PATTERN.matcher(result);
            while (matcher.find()) {
                result = result.replace(HEX_PATTERN.pattern(), "" + ChatColor.of(matcher.group().replaceFirst("&", "")));
            }
            return result;
        } catch (Exception ignored) {
            return input;
        }
    }

    public static boolean isUUID(String input) {
        return UUID_PATTERN.matcher(input).find();
    }
}