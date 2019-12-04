package fr.cocoraid.prodigygui.utils.particle;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.mineacademy.remain.Remain;
import org.mineacademy.remain.internal.ParticleInternals;
import org.mineacademy.remain.model.CompMaterial;
import org.mineacademy.remain.util.MinecraftVersion;
import org.mineacademy.remain.util.MinecraftVersion.V;
import org.mineacademy.remain.util.ReflectionUtil;

/**
 * Wrapper for {@link Particle}
 */
public enum CompParticle {

	EXPLOSION_NORMAL,
	EXPLOSION_LARGE,
	EXPLOSION_HUGE,
	FIREWORKS_SPARK,
	WATER_BUBBLE,
	WATER_SPLASH,
	WATER_WAKE,
	SUSPENDED,
	SUSPENDED_DEPTH,
	CRIT,
	CRIT_MAGIC,
	SMOKE_NORMAL,
	SMOKE_LARGE,
	SPELL,
	SPELL_INSTANT,
	SPELL_MOB,
	SPELL_MOB_AMBIENT,
	SPELL_WITCH,
	DRIP_WATER,
	DRIP_LAVA,
	VILLAGER_ANGRY,
	VILLAGER_HAPPY,
	TOWN_AURA,
	NOTE,
	PORTAL,
	ENCHANTMENT_TABLE,
	FLAME,
	LAVA,
	FOOTSTEP,
	CLOUD,
	REDSTONE,
	SNOWBALL,
	SNOW_SHOVEL,
	SLIME,
	HEART,
	BARRIER,
	ITEM_CRACK,
	BLOCK_CRACK,
	BLOCK_DUST,
	WATER_DROP,
	ITEM_TAKE,
	MOB_APPEARANCE,
	DRAGON_BREATH,
	END_ROD,
	DAMAGE_INDICATOR,
	SWEEP_ATTACK,
	FALLING_DUST,
	TOTEM, SPIT;

	// Hardcoded for best performance
	private static final boolean hasNewMaterials = MinecraftVersion.atLeast(V.v1_13);


	/**
	 * Internal use
	 *
	 * @deprecated use {@link #spawnWithData(Location, CompMaterial)} instead
	 */
	@Deprecated
	private MaterialData data;

	/**
	 * Internal use
	 *
	 * @param data
	 * @return
	 * @deprecated use {@link #spawnWithData(Location, CompMaterial)} instead
	 */
	@Deprecated
	public CompParticle setWoolData(int data) {
		this.data = new MaterialData(CompMaterial.WHITE_WOOL.getMaterial(), (byte) data);

		return this;
	}

	/**
	 * Internal use
	 *
	 * @param mat
	 * @param data
	 * @return
	 * @deprecated use {@link #spawnWithData(Location, CompMaterial)} instead
	 */
	@Deprecated
	public CompParticle setData(Material mat, int data) {
		this.data = new MaterialData(mat, (byte) data);

		return this;
	}



	/**
	 * Spawns the particle at the given location with extra material data
	 *
	 * @param loc
	 * @param data
	 */
	public final void spawnWithData(Location loc, CompMaterial data) {
		if (Remain.hasParticleAPI()) {
			final org.bukkit.Particle p = ReflectionUtil.lookupEnumSilent(org.bukkit.Particle.class, toString());

			if (p != null)
				if (hasNewMaterials)
					loc.getWorld().spawnParticle(p, loc, 1, data.getMaterial().createBlockData());
				else
					loc.getWorld().spawnParticle(p, loc, 1, data.getMaterial().getNewData((byte) data.getData()));

		} else {
			final ParticleInternals p = ReflectionUtil.lookupEnumSilent(ParticleInternals.class, toString());

			if (p != null)
				p.sendColor(loc, DyeColor.getByWoolData((byte) data.getData()).getColor());
		}
	}



	/**
	 * Spawns the particle at the given location only visible for the given player
	 * adding additional extra data
	 *
	 * @param pl
	 * @param loc
	 * @param extra
	 */
	public final void spawnFor(Player pl, Location loc, Double extra) {
		if (Remain.hasParticleAPI()) {
			final org.bukkit.Particle p = ReflectionUtil.lookupEnumSilent(org.bukkit.Particle.class, toString());

			if (p != null)
				pl.spawnParticle(p, loc, 1, 0D, 0D, 0D, extra != null ? extra : 0D);

		} else {
			final ParticleInternals p = ReflectionUtil.lookupEnumSilent(ParticleInternals.class, toString());

			if (p != null)
				p.send(pl, loc, extra != null ? extra.floatValue() : 0F);
		}
	}

	public final void spawnFor(Player pl, Location loc, int amount, double offsetX, double offsetY, double offsetZ, double speed) {
		if (Remain.hasParticleAPI()) {
			final org.bukkit.Particle p = ReflectionUtil.lookupEnumSilent(org.bukkit.Particle.class, toString());

			if (p != null)
				pl.spawnParticle(p, loc, amount, offsetX, offsetY, offsetZ,speed);

		} else {
			final ParticleInternals p = ReflectionUtil.lookupEnumSilent(ParticleInternals.class, toString());

			if (p != null)
				p.send(pl, loc, (float) offsetX,(float)offsetY,(float)offsetZ, (float)speed, amount);
		}
	}
}