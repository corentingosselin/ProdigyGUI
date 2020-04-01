package fr.cocoraid.prodigygui.threedimensionalgui.itemdata;

import org.bukkit.Particle;

public abstract class ParticleData {

    private Particle particle;
    protected int amount = 1;
    private boolean onSelect = false;
    private boolean loop = false;

    public ParticleData(Particle particle) {
        this.particle = particle;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public Particle getParticle() {
        return particle;
    }
}
