package fr.cocoraid.prodigygui.threedimensionalgui.itemdata;

import fr.cocoraid.prodigygui.utils.particle.CompParticle;

public class NormalParticleData extends ParticleData {

    private double offsetX,offsetY,offsetZ = 0;
    private double speed = 0;

    public NormalParticleData(CompParticle particle) {
        super(particle);

    }

    public NormalParticleData setOffsetX(double offsetX) {
        this.offsetX = offsetX;
        return this;
    }

    public NormalParticleData setOffsetY(double offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public NormalParticleData setOffsetZ(double offsetZ) {
        this.offsetZ = offsetZ;
        return this;
    }

    public double getSpeed() {
        return speed;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public double getOffsetZ() {
        return offsetZ;
    }
}
