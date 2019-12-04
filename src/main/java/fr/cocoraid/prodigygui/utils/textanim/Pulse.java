package fr.cocoraid.prodigygui.utils.textanim;

import java.util.ArrayList;

public class Pulse {


    public enum PulseColor {
        MULTI,WHITE,BLACK,RED,YELLOW,BLUE,PINK,GREEN,CYAN;
    }
    private ArrayList<String> frames = new ArrayList<>();
    public Pulse(String text, PulseColor color,int pause) {

        switch (color) {
            case MULTI:
                //Parse all... no break
            case WHITE:
                frames.add("§0" + text);
                frames.add("§8" + text);
                frames.add("§7" + text);
                frames.add("§f" + text);
                for (int i = 0; i < pause; i++) {
                    frames.add("§f" + text);
                }
                frames.add("§f" + text);
                frames.add("§7" + text);
                frames.add("§8" + text);
                frames.add("§0" + text);
                for (int i = 0; i < pause; i++) {
                    frames.add("§0" + text);
                }
                if (color != PulseColor.MULTI) {
                    break;
                }
            case BLACK:
                frames.add("§f" + text);
                frames.add("§7" + text);
                frames.add("§8" + text);
                frames.add("§0" + text);
                for (int i = 0; i < pause; i++) {
                    frames.add("§0" + text);
                }
                frames.add("§0" + text);
                frames.add("§7" + text);
                frames.add("§8" + text);
                frames.add("§0" + text);
                for (int i = 0; i < pause; i++) {
                    frames.add("§0" + text);
                }

                if (color != PulseColor.MULTI) {
                    break;
                }
            case RED:
                frames.add("§0" + text);
                frames.add("§8" + text);
                frames.add("§4" + text);
                frames.add("§c" + text);
                for (int i = 0; i < pause; i++) {
                    frames.add("§c" + text);
                }
                frames.add("§c" + text);
                frames.add("§4" + text);
                frames.add("§8" + text);
                frames.add("§0" + text);
                for (int i = 0; i < pause; i++) {
                    frames.add("§0" + text);
                }
                if (color != PulseColor.MULTI) {
                    break;
                }

            case YELLOW:
                frames.add("§0" + text);
                frames.add("§8" + text);
                frames.add("§6" + text);
                frames.add("§e" + text);
                for (int i = 0; i < pause; i++) {
                    frames.add("§e" + text);
                }
                frames.add("§e" + text);
                frames.add("§6" + text);
                frames.add("§8" + text);
                frames.add("§0" + text);
                for (int i = 0; i < pause; i++) {
                    frames.add("§0" + text);
                }

                if (color != PulseColor.MULTI) {
                    break;
                }

            case BLUE:
                frames.add("§0" + text);
                frames.add("§8" + text);
                frames.add("§1" + text);
                frames.add("§9" + text);
                for (int i = 0; i < pause; i++) {
                    frames.add("§9" + text);
                }
                frames.add("§9" + text);
                frames.add("§1" + text);
                frames.add("§8" + text);
                frames.add("§0" + text);
                for (int i = 0; i < pause; i++) {
                    frames.add("§0" + text);
                }
                if (color != PulseColor.MULTI) {
                    break;
                }

            case PINK:
                frames.add("§0" + text);
                frames.add("§8" + text);
                frames.add("§5" + text);
                frames.add("§d" + text);
                for (int i = 0; i < pause; i++) {
                    frames.add("§d" + text);
                }
                frames.add("§5" + text);
                frames.add("§d" + text);
                frames.add("§8" + text);
                frames.add("§0" + text);
                for (int i = 0; i < pause; i++) {
                    frames.add("§0" + text);
                }

                if (color != PulseColor.MULTI) {
                    break;
                }

            case GREEN:
                frames.add("§0" + text);
                frames.add("§8" + text);
                frames.add("§2" + text);
                frames.add("§a" + text);
                for (int i = 0; i < pause; i++) {
                    frames.add("§a" + text);
                }
                frames.add("§a" + text);
                frames.add("§2" + text);
                frames.add("§8" + text);
                frames.add("§0" + text);
                for (int i = 0; i < pause; i++) {
                    frames.add("§0" + text);
                }

                if (color != PulseColor.MULTI) {
                    break;
                }
            case CYAN:
                frames.add("§0" + text);
                frames.add("§8" + text);
                frames.add("§3" + text);
                frames.add("§b" + text);
                for (int i = 0; i < pause; i++) {
                    frames.add("§b" + text);
                }
                frames.add("§b" + text);
                frames.add("§3" + text);
                frames.add("§8" + text);
                frames.add("§0" + text);
                for (int i = 0; i < pause; i++) {
                    frames.add("§0" + text);
                }

                break;

        }
    }
    private int frame = 0;
    public String next() {
        if(frame >= frames.size())
            frame = 0;
        String s = frames.get(frame);
        frame++;
        return s;
    }

    public ArrayList<String> getFrames() {
        return frames;
    }
}
