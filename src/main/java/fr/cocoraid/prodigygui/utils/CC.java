package fr.cocoraid.prodigygui.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CC {


    public static final Map<String, Color> COLOURS = new HashMap<String, Color>() {{
        put("RED", Color.RED);
        put("GREEN", Color.GREEN);
        put("BLUE", Color.BLUE);
        put("WHITE", Color.AQUA);
        put("BLACK", Color.BLACK);
        put("FUSCHSIA", Color.FUCHSIA);
        put("LIME", Color.LIME);
        put("GRAY", Color.GRAY);
        put("MAROON", Color.MAROON);
        put("OLIVE", Color.OLIVE);
        put("NAVY", Color.NAVY);
        put("ORANGE", Color.ORANGE);
        put("PURPLE", Color.PURPLE);
        put("SILVER", Color.SILVER);
        put("TEAL", Color.TEAL);
        put("WHITE", Color.WHITE);
        put("YELLOW", Color.YELLOW);
    }};


    public static String aqua = ChatColor.AQUA + "";
    public static String black = ChatColor.BLACK + "";
    public static String blue = ChatColor.BLUE + "";
    public static String d_aqua = ChatColor.DARK_AQUA + "";
    public static String d_blue = ChatColor.DARK_BLUE + "";
    public static String d_gray = ChatColor.DARK_GRAY + "";
    public static String d_green = ChatColor.DARK_GREEN + "";
    public static String d_purple = ChatColor.DARK_PURPLE + "";
    public static String d_red = ChatColor.DARK_RED + "";
    public static String gold = ChatColor.GOLD + "";
    public static String gray = ChatColor.GRAY + "";
    public static String green = ChatColor.GREEN + "";
    public static String l_purple = ChatColor.LIGHT_PURPLE + "";
    public static String red = ChatColor.RED + "";
    public static String white = ChatColor.WHITE + "";
    public static String yellow = ChatColor.YELLOW + "";

    public static String bold = ChatColor.BOLD + "";
    public static String italic = ChatColor.ITALIC + "";
    public static String magic = ChatColor.MAGIC + "";
    public static String reset = ChatColor.RESET + "";
    public static String underline = ChatColor.UNDERLINE + "";
    public static String strike = ChatColor.STRIKETHROUGH + "";

    public static String arrow = "\u27bd";

    public static String headkey = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv";

    /**
     * donner la couleur arc en ciel
     *
     * @param string
     * @return
     */
    public static String rainbowlize(String string) {
        int lastColor = 0;
        int currColor;
        String newString = "";
        String colors = "123456789abcde";
        for (int i = 0; i < string.length(); i++) {
            do {
                currColor = new Random().nextInt(colors.length() - 1) + 1;
            }
            while (currColor == lastColor);
            newString += ChatColor.RESET.toString() + ChatColor.getByChar(colors.charAt(currColor)) + "" + string.charAt(i);
        }
        return newString;
    }



    public static String colored(String s) {
        if(s == null) return null;
        return s.replace("&", "§");
    }


}
