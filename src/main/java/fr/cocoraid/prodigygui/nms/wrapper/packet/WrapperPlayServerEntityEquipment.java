package fr.cocoraid.prodigygui.nms.wrapper.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import fr.cocoraid.prodigygui.nms.AbstractPacket;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WrapperPlayServerEntityEquipment extends AbstractPacket {
	public static final PacketType TYPE =
			PacketType.Play.Server.ENTITY_EQUIPMENT;

	public WrapperPlayServerEntityEquipment() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerEntityEquipment(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Entity ID.
	 * <p>
	 * Notes: entity's ID
	 * 
	 * @return The current Entity ID
	 */
	public int getEntityID() {
		return handle.getIntegers().read(0);
	}

	/**
	 * Set Entity ID.
	 * 
	 * @param value - new value.
	 */
	public void setEntityID(int value) {
		handle.getIntegers().write(0, value);
	}

	/**
	 * Retrieve the entity of the painting that will be spawned.
	 * 
	 * @param world - the current world of the entity.
	 * @return The spawned entity.
	 */
	public Entity getEntity(World world) {
		return handle.getEntityModifier(world).read(0);
	}

	/**
	 * Retrieve the entity of the painting that will be spawned.
	 * 
	 * @param event - the packet event.
	 * @return The spawned entity.
	 */
	public Entity getEntity(PacketEvent event) {
		return getEntity(event.getPlayer().getWorld());
	}

	public EnumWrappers.ItemSlot getSlot() {
		return handle.getItemSlots().read(0);
	}

	public void setSlot(EnumWrappers.ItemSlot value) {
		handle.getItemSlots().write(0, value);
	}

	public void setSlot(int value) {
		handle.getIntegers().write(1, value);
	}
	/**
	 * Retrieve Item.
	 * <p>
	 * Notes: item in slot format
	 * 
	 * @return The current Item
	 */
	public ItemStack getItem() {
		return handle.getItemModifier().read(0);
	}


	/**
	 * Set Item.
	 * 
	 * @param value - new value.
	 */
	public void setItem(ItemStack value) {
		handle.getItemModifier().write(0, value);
	}

	private List<Pair<ItemSlot, ItemStack>> itemList = new ArrayList<>();
	public void setItem(ItemSlot slot, ItemStack stack) {
		Pair<ItemSlot, ItemStack> itemPair = new Pair<>(slot, stack);
		Optional<Pair<ItemSlot, ItemStack>> optPair = this.itemList.stream().filter(entry -> entry.getFirst() == slot).findFirst();
		if(optPair.isPresent()) this.itemList.remove(optPair.get());
		this.itemList.add(itemPair);
		handle.getSlotStackPairLists().write(0, this.itemList);
	}
}