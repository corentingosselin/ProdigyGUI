package fr.cocoraid.prodigygui.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class UtilItem {

    public static ItemStack skullTextured(String base64) {
            UUID id = UUID.nameUUIDFromBytes(base64.getBytes());
            int less = (int) id.getLeastSignificantBits();
            int most = (int) id.getMostSignificantBits();
            return Bukkit.getUnsafe().modifyItemStack(
                    new ItemStack(Material.PLAYER_HEAD),
                    "{SkullOwner:{Id:[I;" + (less * most) + "," + (less >> 23) + "," + (most / less) + "," + (most * 8731) + "],Properties:{textures:[{Value:\""  + "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv" + base64 + "\"}]}}}"
            );

    }
}
