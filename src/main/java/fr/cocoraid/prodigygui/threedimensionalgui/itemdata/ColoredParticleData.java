package fr.cocoraid.prodigygui.threedimensionalgui.itemdata;

import fr.cocoraid.prodigygui.utils.particle.CompParticle;
import org.apache.commons.lang.Validate;
import org.bukkit.Particle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColoredParticleData extends ParticleData {


    private static List<CompParticle> colorableParticles = new ArrayList<>(Arrays.asList(
            CompParticle.REDSTONE,
            CompParticle.SPELL_MOB,
            CompParticle.SPELL_MOB_AMBIENT
    ));

    private int r,g,b = 0;
    private double radius = 0.5;

    public ColoredParticleData(CompParticle particle) {
        super(particle);
        Validate.isTrue(colorableParticles.contains(particle));
        this.amount = 0;
    }

    public ColoredParticleData setB(int b) {
        this.b = b;
        return this;
    }

    public ColoredParticleData setG(int g) {
        this.g = g;
        return this;
    }

    public ColoredParticleData setR(int r) {
        this.r = r;
        return this;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public int getB() {
        return b;
    }

    public int getG() {
        return g;
    }

    public int getR() {
        return r;
    }

    public static List<CompParticle> getColorableParticles() {
        return colorableParticles;
    }
}
