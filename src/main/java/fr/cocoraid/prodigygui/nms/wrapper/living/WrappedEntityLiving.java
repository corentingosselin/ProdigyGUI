package fr.cocoraid.prodigygui.nms.wrapper.living;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
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
        if(VersionChecker.isHigherOrEqualThan(VersionChecker.v1_14_R1)) {
            Optional<?> opt = Optional.of(WrappedChatComponent.fromChatMessage(name)[0].getHandle());
            dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), opt);

        } else if(VersionChecker.isHigherOrEqualThan(VersionChecker.v1_13_R1)) {
            dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2,
                            WrappedDataWatcher.Registry.getChatComponentSerializer(true))
                    , Optional.ofNullable(WrappedChatComponent.fromChatMessage(name)[0].getHandle()));
        }
        else {
            setDataWatcherObject(String.class, 2, name);
        }
    }


    public void setCustomNameVisible(boolean visible) {
        if(VersionChecker.isLowerOrEqualThan(VersionChecker.v1_8_R3))
            setDataWatcherObject(Byte.class,3,(byte) (visible ? 1 : 0));
        else
            setDataWatcherObject(Boolean.class,3,visible);

    }
    public void setInvisible(boolean invisible) {
        setDataWatcherObject(Byte.class,0,invisible ? (byte) 0x20 : (byte) 0);
    }
    public void despawn() {
        destroyPacket.sendPacket(player);
    }

    public void equip(EnumWrappers.ItemSlot slot, ItemStack item) {
        WrapperPlayServerEntityEquipment equipPacket = equipments.get(slot);
        equipPacket.setItem(item);
        if(slot == EnumWrappers.ItemSlot.HEAD) {
            if(VersionChecker.isLowerOrEqualThan(VersionChecker.v1_8_R3))
                equipPacket.setSlot(4);
            else equipPacket.setSlot(slot);
        } else equipPacket.setSlot(slot);
        equipPacket.sendPacket(player);
    }



    public void setDataWatcherObject(Class<?> type, int objectIndex, Object object) {
        if(VersionChecker.isLowerOrEqualThan(VersionChecker.v1_8_R3)) {
            dataWatcher.setObject(objectIndex,object);
        } else {
            WrappedDataWatcher.WrappedDataWatcherObject watcherObject = new WrappedDataWatcher.WrappedDataWatcherObject(objectIndex, WrappedDataWatcher.Registry.get(type));
            dataWatcher.setObject(watcherObject, object);
        }

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
