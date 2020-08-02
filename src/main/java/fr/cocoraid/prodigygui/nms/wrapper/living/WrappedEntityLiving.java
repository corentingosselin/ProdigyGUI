package fr.cocoraid.prodigygui.nms.wrapper.living;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import fr.cocoraid.prodigygui.nms.EIDGen;
import fr.cocoraid.prodigygui.nms.wrapper.packet.*;
import fr.cocoraid.prodigygui.utils.UtilMath;
import fr.cocoraid.prodigygui.utils.VersionChecker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class WrappedEntityLiving {


    private static WrappedDataWatcher.Serializer
            itemSerializer,
            intSerializer,
            byteSerializer,
            stringSerializer,
            booleanSerializer;

    static {
        if (VersionChecker.isHigherOrEqualThan(VersionChecker.v1_9_R1)) {
            itemSerializer = WrappedDataWatcher.Registry.get(MinecraftReflection.getItemStackClass());
            intSerializer = WrappedDataWatcher.Registry.get(Integer.class);
            byteSerializer = WrappedDataWatcher.Registry.get(Byte.class);
            stringSerializer = WrappedDataWatcher.Registry.get(String.class);
            booleanSerializer = WrappedDataWatcher.Registry.get(Boolean.class);
        }
    }

    private WrapperPlayServerSpawnEntityLiving spawnPacket;
    private WrapperPlayServerEntityDestroy destroyPacket;
    private WrapperPlayServerEntityMetadata metaPacket;
    private WrapperPlayServerEntityTeleport teleportPacket;
    private WrapperPlayServerEntityHeadRotation yawPacket;

    private WrappedDataWatcher dataWatcher;

    protected Map<EnumWrappers.ItemSlot, WrapperPlayServerEntityEquipment> equipments = new HashMap<>();

    private Location location;
    private Player player;
    /**
     * @check https://wiki.vg/Entity_metadata#Mobs
     */
    private int typeID;
    private int id;

    public WrappedEntityLiving(Location location,Player player, int typeID) {
        this.location = location;
        this.id = EIDGen.generateEID();
        this.typeID = typeID;
        this.player = player;

        this.spawnPacket = new WrapperPlayServerSpawnEntityLiving();
        spawnPacket.setEntityID(id);
        spawnPacket.setType(typeID);
        spawnPacket.setPitch(location.getPitch());
        spawnPacket.setHeadPitch(location.getPitch());
        spawnPacket.setYaw(location.getYaw());
        spawnPacket.setX(location.getX());
        spawnPacket.setY(location.getY());
        spawnPacket.setZ(location.getZ());

        this.yawPacket = new WrapperPlayServerEntityHeadRotation();
        yawPacket.setEntityID(id);
        yawPacket.setHeadYaw(UtilMath.toPackedByte(location.getYaw()));

        this.destroyPacket = new WrapperPlayServerEntityDestroy();
        destroyPacket.setEntityIds(new int[] {id});

        this.dataWatcher = new WrappedDataWatcher();

        this.metaPacket = new WrapperPlayServerEntityMetadata();
        metaPacket.setEntityID(id);

        this.teleportPacket = new WrapperPlayServerEntityTeleport();


        for (EnumWrappers.ItemSlot itemSlot : EnumWrappers.ItemSlot.values()) {
            WrapperPlayServerEntityEquipment equip = new WrapperPlayServerEntityEquipment();
            equip.setEntityID(id);
            equipments.put(itemSlot,equip);
        }

    }

    public void teleport(Location newLocation) {
        teleportPacket.setEntityID(id);
        teleportPacket.setX(newLocation.getX());
        teleportPacket.setY(newLocation.getY());
        teleportPacket.setZ(newLocation.getZ());
        teleportPacket.setYaw(newLocation.getYaw());
        teleportPacket.setPitch(newLocation.getPitch());
        teleportPacket.sendPacket(player);
        this.location = newLocation;

        //update spawn location too
        spawnPacket.setPitch(location.getPitch());
        spawnPacket.setHeadPitch(location.getPitch());
        spawnPacket.setYaw(location.getYaw());
        spawnPacket.setX(location.getX());
        spawnPacket.setY(location.getY());
        spawnPacket.setZ(location.getZ());

        yawPacket.setHeadYaw(UtilMath.toPackedByte(location.getYaw()));
    }

    public void fakeTeleport(Location l) {
        teleportPacket.setEntityID(id);
        teleportPacket.setX(l.getX());
        teleportPacket.setY(l.getY());
        teleportPacket.setZ(l.getZ());
        teleportPacket.setYaw(l.getYaw());
        teleportPacket.setPitch(l.getPitch());
        teleportPacket.sendPacket(player);
    }

    public void updateYaw() {
        yawPacket.sendPacket(player);
    }

    public void spawnClient(Player client) {
        spawnPacket.sendPacket(client);
        //armorstand does not need it...
        if(typeID != 0)
            yawPacket.sendPacket(client);
    }

    public void updateForClient(Player client) {

    }


    public void despawnClient(Player client) {
        destroyPacket.sendPacket(client);

    }

    public void spawn() {
        spawnPacket.sendPacket(player);
        if(typeID != 0)
            yawPacket.sendPacket(player);

    }



    public void setCustomName(String name) {

        if(VersionChecker.isHigherOrEqualThan(VersionChecker.v1_13_R1)) {
            dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2,
                            WrappedDataWatcher.Registry.getChatComponentSerializer(true))
                    , Optional.ofNullable(WrappedChatComponent.fromChatMessage(name)[0].getHandle()));
        }
        else {
            dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, stringSerializer), name);
        }
    }


    public void setCustomNameVisible(boolean visible) {
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, booleanSerializer), visible);


    }
    public void setInvisible(boolean invisible) {
        setDataWatcherObject(Byte.class,0,invisible ? (byte) 0x20 : (byte) 0);
    }
    public void despawn() {
        destroyPacket.sendPacket(player);
    }

    public void equip(EnumWrappers.ItemSlot slot, ItemStack item) {
        WrapperPlayServerEntityEquipment equipPacket = equipments.get(slot);
        if(VersionChecker.isHigherOrEqualThan(VersionChecker.v1_16_R1)) {
            equipPacket.setItem(slot, item);
        } else {
            equipPacket.setItem(item);
            equipPacket.setSlot(slot);
        }
        equipPacket.sendPacket(player);
    }



    public void setDataWatcherObject(Class<?> type, int objectIndex, Object object) {
        WrappedDataWatcher.WrappedDataWatcherObject watcherObject = new WrappedDataWatcher.WrappedDataWatcherObject(objectIndex, WrappedDataWatcher.Registry.get(type));
        dataWatcher.setObject(watcherObject, object);

    }


    public void sendUpdatedmetatada() {
        metaPacket.setMetadata(dataWatcher.getWatchableObjects());
        metaPacket.sendPacket(player);
    }

    public WrappedDataWatcher getDataWatcher() {
        return dataWatcher;
    }
    

    public void setLocation(Location location) {
        spawnPacket.setPitch(location.getPitch());
        spawnPacket.setHeadPitch(location.getPitch());
        spawnPacket.setYaw(location.getYaw());
        spawnPacket.setX(location.getX());
        spawnPacket.setY(location.getY());
        spawnPacket.setZ(location.getZ());
        this.location = location;
        yawPacket.setHeadYaw(UtilMath.toPackedByte(location.getYaw()));
    }

    public int getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }
}
