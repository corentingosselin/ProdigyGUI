package fr.cocoraid.prodigygui.threedimensionalgui.itemdata;

import org.bukkit.Sound;
import org.mineacademy.remain.model.CompSound;

public class SoundData {

    private Sound sound;
    private float volume = 1;
    private float pitch = 1;

    public SoundData(String sound) {
        this.sound = CompSound.convert(sound);
    }

    public SoundData setVolume(float volume) {
        this.volume = volume;
        return this;
    }

    public SoundData setPitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    public float getPitch() {
        return pitch;
    }

    public float getVolume() {
        return volume;
    }

    public Sound getSound() {
        return sound;
    }
}
