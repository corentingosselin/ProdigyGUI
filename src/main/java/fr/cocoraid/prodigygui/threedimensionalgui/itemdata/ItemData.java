package fr.cocoraid.prodigygui.threedimensionalgui.itemdata;

import fr.cocoraid.prodigygui.utils.CC;
import fr.cocoraid.prodigygui.utils.SkullCreator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemData {

    /**
     * Required
     */

    private String displayname;


    private ItemStack displayItem;



    /**
     * Optionals
     */
    private List<String> command;
    private int price;
    private String permission;
    private String nopermissionmessage;
    private SoundData soundData;
    private ParticleData particleData;
    private String lore;



    public ItemData(String displayname,  Material ID) {
        this.displayname = CC.colored(displayname);
        this.displayItem = new ItemStack(ID);
    }

    public ItemData(String displayname,  String skulltexture) {
        this.displayname = CC.colored(displayname);
        this.displayItem = SkullCreator.itemFromBase64(skulltexture);
    }

    public void setCommand(String command) {
        if(command == null)
            this.command = null;
        else
            this.command = new ArrayList<>(Arrays.asList(command.split("; ")));
    }


    public void setPrice(int price) {
        this.price = price;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setNopermissionmessage(String nopermissionmessage) {
        this.nopermissionmessage = CC.colored(nopermissionmessage);
    }

    public void setSoundData(SoundData soundData) {
        this.soundData = soundData;
    }

    public void setLore(String lore) {
        this.lore = CC.colored(lore);
    }



    public String getDisplayname() {
        return displayname;
    }

    public List<String> getCommands() {
        return command;
    }

    public int getPrice() {
        return price;
    }


    public String getNopermissionmessage() {
        return nopermissionmessage;
    }

    public String getPermission() {
        return permission;
    }


    public String getLore() {
        return lore;
    }

    public void setParticleData(ParticleData particleData) {
        this.particleData = particleData;
    }

    public ParticleData getParticleData() {
        return particleData;
    }

    public ItemStack getDisplayItem() {
        return displayItem;
    }

    public SoundData getSoundData() {
        return soundData;
    }
}
