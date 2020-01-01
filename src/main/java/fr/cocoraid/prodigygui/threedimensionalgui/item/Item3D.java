package fr.cocoraid.prodigygui.threedimensionalgui.item;

import com.comphenix.protocol.wrappers.EnumWrappers;
import fr.cocoraid.prodigygui.bridge.PlaceholderAPIBridge;
import fr.cocoraid.prodigygui.nms.wrapper.living.WrapperEntityArmorStand;
import fr.cocoraid.prodigygui.nms.wrapper.living.WrapperEntitySlime;
import fr.cocoraid.prodigygui.threedimensionalgui.itemdata.ItemData;
import fr.cocoraid.prodigygui.utils.VersionChecker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;


/**
 * Created by cocoraid on 02/07/2016.
 */
public class Item3D {



    private Player player;
    private Location location;


    private WrapperEntityArmorStand itemDisplay;
    private WrapperEntityArmorStand displayName;
    private WrapperEntitySlime selector;


    private ItemData data;
    private String name;
    private ItemStack item;
    private boolean spawned = false;
    private boolean enable;
    private boolean isSmall = false;
    private float yawRotation = 0;

    public Item3D(Player player, ItemData data) {
        this.data = data;
        this.player = player;
        this.item =  data.getDisplayItem();
        this.name = data.getDisplayname();
        this.enable = true;
        Location temp = player.getEyeLocation();
        temp.setPitch(0);
        location = player.getEyeLocation().add(temp.getDirection());

        this.displayName = new WrapperEntityArmorStand(temp,player);
        displayName.setCustomName(PlaceholderAPIBridge.setPlaceholders(name,player));
        displayName.setCustomNameVisible(true);
        displayName.setInvisible(true);
        displayName.setMarker(true);
        displayName.setSmall(true);

        this.itemDisplay = new WrapperEntityArmorStand(temp,player);
        itemDisplay.setInvisible(true);

        this.selector = new WrapperEntitySlime(temp,player);
        selector.setSize(2);
        selector.setInvisible(true);
    }

    public void setSmall() {
        itemDisplay.setSmall(true);
        this.isSmall = true;
    }

    /**
     * The location must include yaw
     * @param loc
     * @return
     */
    public Item3D setPosition(Location loc) {

        if (item.getType().isBlock()
                || item.getType() == Material.PLAYER_HEAD
                || item.getType() == Material.CREEPER_HEAD
                || item.getType() == Material.DRAGON_HEAD
                || item.getType() == Material.ZOMBIE_HEAD
                ) {
            setSmall();
        }
        Location itemLoc = loc.clone().subtract(0, isSmall ? 0.7 : 2.2,0);
        //isItem does not exist for 1.11
        if( (VersionChecker.isHigherOrEqualThan(VersionChecker.v1_12_R1) && item.getType().isItem() && !item.getType().isBlock())
                || (VersionChecker.isLowerOrEqualThan(VersionChecker.v1_11_R1) && !item.getType().isBlock()))  {
            itemLoc.add(itemLoc.getDirection().normalize().multiply(0.2));
        }

        double tosubDisplay = 0.3;
        Location l = itemLoc.clone().subtract(0,tosubDisplay,0);
        l.setYaw(l.getYaw() + data.getRotation());
        itemDisplay.setLocation(l);

        itemDisplay.setMarker(true);
        itemDisplay.spawn();
        itemDisplay.equip(EnumWrappers.ItemSlot.HEAD,item);
        itemDisplay.sendUpdatedmetatada();




        displayName.setLocation(loc);
        displayName.setMarker(true);
        displayName.spawn();
        displayName.sendUpdatedmetatada();


        selector.setLocation(loc.clone().subtract(0,0.3,0)); // .add(toadd)
        selector.spawn();
        selector.sendUpdatedmetatada();
        location = loc;
        this.spawned = true;

        return this;
    }

    public void move(boolean back) {
        if (back) {
            itemDisplay.fakeTeleport(itemDisplay.getLocation());
            selector.fakeTeleport(selector.getLocation());
            displayName.teleport(displayName.getLocation());
        } else {
            Location l = getLocation().clone();
            Vector v = player.getLocation().toVector().subtract(l.toVector()).normalize();
            l.setDirection(v);
            l.setPitch(0F);
            Vector toadd = l.getDirection().multiply(0.3);
            itemDisplay.fakeTeleport(itemDisplay.getLocation().clone().add(toadd));
            displayName.fakeTeleport(displayName.getLocation().clone().add(toadd));
            selector.fakeTeleport(selector.getLocation().clone().add(toadd));
        }
    }


    public void teleport(Location loc) {
        Location l = isSmall ? loc.clone().add(0, 1.3, 0) : loc;
        itemDisplay.teleport(l);
        displayName.teleport(loc.clone().add(0, 0.3f, 0));
        selector.teleport(loc.clone().add(0, 1.8, 0));
        this.location = loc;
    }



    public Location getLocation() {
        return location;
    }

    public void remove() {
        if (itemDisplay != null)
            itemDisplay.despawn();

        if (displayName != null)
            displayName.despawn();

        if (selector != null)
            selector.despawn();


        this.spawned = false;

    }


    public String getName() {
        return name;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isSpawned() {
        return spawned;
    }

    public boolean isEnable() {
        return enable;
    }


    public WrapperEntitySlime getSelector() {
        return selector;
    }

    public float getYawRotation() {
        return yawRotation;
    }

    public ItemStack getItem() {
        return item;
    }

    public ItemData getData() {
        return data;
    }
}
