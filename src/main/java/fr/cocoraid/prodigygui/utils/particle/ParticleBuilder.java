package fr.cocoraid.prodigygui.utils.particle;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;


public abstract class ParticleBuilder {

    protected Location location;
    protected int amount = 1;
    protected Particle particle;


    public ParticleBuilder(Location location) {
        this.location = location;
    }

    public ParticleBuilder setParticle(Particle particle) {
        this.particle = particle;
        return this;
    }

    public ParticleBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }



    public abstract void sendParticle(Player player);





}


