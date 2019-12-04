package fr.cocoraid.prodigygui.threedimensionalgui.itemdata;

import fr.cocoraid.prodigygui.utils.particle.CompParticle;

public abstract class ParticleData {

    private CompParticle particle;
    protected int amount = 1;

    public ParticleData(CompParticle particle) {
        this.particle = particle;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public CompParticle getParticle() {
        return particle;
    }
}
