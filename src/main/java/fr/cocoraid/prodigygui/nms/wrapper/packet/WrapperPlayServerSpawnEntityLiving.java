package fr.cocoraid.prodigygui.nms.wrapper.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.PacketConstructor;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import fr.cocoraid.prodigygui.nms.AbstractPacket;
import fr.cocoraid.prodigygui.utils.VersionChecker;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class WrapperPlayServerSpawnEntityLiving extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Server.SPAWN_ENTITY_LIVING;

	private static PacketConstructor entityConstructor;

	public WrapperPlayServerSpawnEntityLiving() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerSpawnEntityLiving(PacketContainer packet) {
		super(packet, TYPE);
	}

	public WrapperPlayServerSpawnEntityLiving(Entity entity) {
		super(fromEntity(entity), TYPE);
	}

	// Useful constructor
	private static PacketContainer fromEntity(Entity entity) {
		if (entityConstructor == null)
			entityConstructor =
					ProtocolLibrary.getProtocolManager()
							.createPacketConstructor(TYPE, entity);
		return entityConstructor.createPacket(entity);
	}

	/**
	 * Retrieve entity ID.
	 *
	 * @return The current EID
	 */
	public int getEntityID() {
		return handle.getIntegers().read(0);
	}

	/**
	 * Retrieve the entity that will be spawned.
	 *
	 * @param world - the current world of the entity.
	 * @return The spawned entity.
	 */
	public Entity getEntity(World world) {
		return handle.getEntityModifier(world).read(0);
	}

	/**
	 * Retrieve the entity that will be spawned.
	 *
	 * @param event - the packet event.
	 * @return The spawned entity.
	 */
	public Entity getEntity(PacketEvent event) {
		return getEntity(event.getPlayer().getWorld());
	}

	public UUID getUniqueId() {
		return handle.getUUIDs().read(0);
	}

	public void setUniqueId(UUID value) {
		if(VersionChecker.isLowerOrEqualThan(VersionChecker.v1_8_R3))
			handle.getUUIDs().write(0, value);
	}

	/**
	 * Set entity ID.
	 *
	 * @param value - new value.
	 */
	public void setEntityID(int value) {
		handle.getIntegers().write(0, value);
	}

	/**
	 * Retrieve the type of mob.
	 *
	 * @return The current Type
	 */
	@SuppressWarnings("deprecation")
	public int getTypeID() {
		return handle.getIntegers().read(1);
	}

	/**
	 * Set the type of mob.
	 *
	 * @param typeID - new value.
	 */
	@SuppressWarnings("deprecation")
	public void setType(int typeID) {

		handle.getIntegers().write(1, typeID);


	}

	/**
	 * Retrieve the x position of the object.
	 * <p>
	 * Note that the coordinate is rounded off to the nearest 1/32 of a meter.
	 *
	 * @return The current X
	 */
	public double getX() {
		return handle.getDoubles().read(0);

	}

	/**
	 * Set the x position of the object.
	 *
	 * @param value - new value.
	 */
	public void setX(double value) {
		if(VersionChecker.isLowerOrEqualThan(VersionChecker.v1_8_R3))
			handle.getIntegers().write(2, (int) Math.floor(value * 32.0D));
		else
			handle.getDoubles().write(0, value);
	}

	/**
	 * Retrieve the y position of the object.
	 * <p>
	 * Note that the coordinate is rounded off to the nearest 1/32 of a meter.
	 *
	 * @return The current y
	 */
	public double getY() {
		return handle.getDoubles().read(1);
	}

	/**
	 * Set the y position of the object.
	 *
	 * @param value - new value.
	 */
	public void setY(double value) {
		if(VersionChecker.isLowerOrEqualThan(VersionChecker.v1_8_R3))
			handle.getIntegers().write(3, (int) Math.floor(value * 32.0D));
		else
			handle.getDoubles().write(1, value);
	}

	/**
	 * Retrieve the z position of the object.
	 * <p>
	 * Note that the coordinate is rounded off to the nearest 1/32 of a meter.
	 *
	 * @return The current z
	 */
	public double getZ() {
		return handle.getDoubles().read(2);
	}

	/**
	 * Set the z position of the object.
	 *
	 * @param value - new value.
	 */
	public void setZ(double value) {
		if(VersionChecker.isLowerOrEqualThan(VersionChecker.v1_8_R3))
			handle.getIntegers().write(4, (int) Math.floor(value * 32.0D));
		else
			handle.getDoubles().write(2, value);
	}

	/**
	 * Retrieve the yaw.
	 *
	 * @return The current Yaw
	 */
	public float getYaw() {
		return (handle.getBytes().read(0) * 360.F) / 256.0F;
	}

	/**
	 * Set the yaw of the spawned mob.
	 *
	 * @param value - new yaw.
	 */
	public void setYaw(float value) {
		handle.getBytes().write(0, (byte) (value * 256.0F / 360.0F));


	}

	/**
	 * Retrieve the pitch.
	 *
	 * @return The current pitch
	 */
	public float getPitch() {
		return (handle.getBytes().read(1) * 360.F) / 256.0F;
	}

	/**
	 * Set the pitch of the spawned mob.
	 *
	 * @param value - new pitch.
	 */
	public void setPitch(float value) {
		handle.getBytes().write(1, (byte) (value * 256.0F / 360.0F));
	}

	/**
	 * Retrieve the yaw of the mob's head.
	 *
	 * @return The current yaw.
	 */
	public float getHeadPitch() {
		return (handle.getBytes().read(2) * 360.F) / 256.0F;
	}

	/**
	 * Set the yaw of the mob's head.
	 *
	 * @param value - new yaw.
	 */
	public void setHeadPitch(float value) {
		if(VersionChecker.isLowerOrEqualThan(VersionChecker.v1_8_R3))
			handle.getBytes().write(1, (byte) (value * 256.0F / 360.0F));
		else
			handle.getBytes().write(2, (byte) (value * 256.0F / 360.0F));

	}


	/**
	 * Retrieve the data watcher.
	 * <p>
	 * Content varies by mob, see Entities.
	 *
	 * @return The current Metadata
	 */
	public WrappedDataWatcher getMetadata() {
		return handle.getDataWatcherModifier().read(0);
	}

	/**
	 * Set the data watcher.
	 *
	 * @param value - new value.
	 */
	public void setMetadata(WrappedDataWatcher value) {
		handle.getDataWatcherModifier().write(0, value);
	}
}