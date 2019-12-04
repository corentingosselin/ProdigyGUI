package fr.cocoraid.prodigygui.utils.particle;

import fr.cocoraid.prodigygui.threedimensionalgui.itemdata.ColoredParticleData;
import fr.cocoraid.prodigygui.utils.UtilMath;
import fr.cocoraid.prodigygui.utils.VersionChecker;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.mineacademy.remain.internal.ParticleInternals;

public class ColoredParticle extends ParticleBuilder {

    private Color color;
    private double radius = 0;
    public ColoredParticle(Location location) {
        super(location);
        if(ColoredParticleData.getColorableParticles().contains(particle)) {
            System.out.println("Error: Particle " + particle.name() + " is not available for " + getClass().getSimpleName());
            return;
        }
    }

    public ColoredParticle setColor(Color color) {
        this.color = color;
        return this;
    }

    public ColoredParticle setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    public ColoredParticle setColor(int r, int g, int b) {
        this.color = Color.fromBGR(b,g,r);
        return this;
    }

    @Override
    public void sendParticle(Player player) {

        if(VersionChecker.isHigherOrEqualThan(VersionChecker.v1_13_R1)) {
            if (particle == CompParticle.REDSTONE) {
                Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1);
                for (int i = 0; i < amount; i++) {
                    Location loc = radius > 0 ? location.clone().add(UtilMath.randomRange(-radius, radius), UtilMath.randomRange(-radius, radius), UtilMath.randomRange(-radius, radius)) : location;
                    player.spawnParticle(Particle.REDSTONE, loc, 0, dustOptions);
                }
            } else if (particle == CompParticle.SPELL_MOB_AMBIENT || particle == CompParticle.SPELL_MOB) {
                double red = color.getRed() / 255D;
                double green = color.getGreen() / 255D;
                double blue = color.getBlue() / 255D;
                for (int i = 0; i < amount; i++) {
                    Location loc = radius > 0 ? location.clone().add(UtilMath.randomRange(-radius, radius), UtilMath.randomRange(-radius, radius), UtilMath.randomRange(-radius, radius)) : location;
                    player.spawnParticle(Particle.SPELL_MOB, loc, 0, red, green, blue, 1);
                }
            }
        } else {
            for (int i = 0; i < amount; i++) {
                Location loc = radius > 0 ? location.clone().add(UtilMath.randomRange(-radius, radius), UtilMath.randomRange(-radius, radius), UtilMath.randomRange(-radius, radius)) : location;
                ParticleInternals.valueOf(particle.name()).sendColor(player, loc, color);
            }
        }
    }
}
