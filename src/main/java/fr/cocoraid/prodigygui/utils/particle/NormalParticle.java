package fr.cocoraid.prodigygui.utils.particle;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.mineacademy.remain.internal.ParticleInternals;

public class NormalParticle extends ParticleBuilder {


    private Vector offset = new Vector(0,0,0);
    private double speed = 0;
    public NormalParticle(Location location) {
        super(location);
    }


    public NormalParticle setOffset(Vector offset) {
        this.offset = offset;
        return this;
    }

    public NormalParticle setSpeed(double speed) {
        this.speed = speed;
        return this;
    }

    public double getSpeed() {
        return speed;
    }

    @Override
    public void sendParticle(Player player) {
        particle.spawnFor(player,location,amount,offset.getX(),offset.getY(),offset.getZ(),speed);

    }
}
