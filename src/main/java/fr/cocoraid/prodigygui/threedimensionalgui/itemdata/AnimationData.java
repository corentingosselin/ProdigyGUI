package fr.cocoraid.prodigygui.threedimensionalgui.itemdata;

import fr.cocoraid.prodigygui.threedimensionalgui.item.Animation;

public class AnimationData {

    private Animation animation;
    private double speed = 0;
    private boolean onSelect = false;

    public AnimationData(Animation animation) {
        this.animation = animation;
    }


    public AnimationData setSpeed(double speed) {
        this.speed = speed;
        return this;
    }

}
