package fr.cocoraid.prodigygui.utils.textanim;


import java.util.ArrayList;

public class Glow   {



    //Default glow options.
           /* put("normal", "§7§l");
            put("start","§d§l");
            put("middle","§5§l");
            put("end","§d§l");
            put("size","5");
            put("pause","10");*/


    private ArrayList<String> frames = new ArrayList<>();
    private int frame = 0;
    public Glow(String text, String normaltextcolor, String startColor, String middleColor, String endColor, int size, int pause) {



        String normalFormat = normaltextcolor;
        String startFormat =  startColor;
        String middleFormat = middleColor;
        String endFormat =    endColor;


        int length = text.length();
        int iterations = length + size + 2 + 1;

        int startsub = 0;
        int endsub = 0;

        int counter = 0;

        for (int pos = 0; pos < iterations; ++pos) {
            if (pos > 1 && pos < iterations - 2) {
                if (counter >= length - size) {
                    startsub -= 1;
                    endsub += 1;
                } else {
                    if (startsub + 1 > size) {
                        startsub = size;
                        counter += 1;
                    } else {
                        startsub += 1;
                    }
                }

            }

            String startPart = "", middlePart = "", endPart = "", before = "", last = "";

            if (pos >= iterations - length - 1 && pos < iterations - 1) {
                startPart = text.substring(pos - (iterations - length - 1), pos - (iterations - length) + 2);
            }

            if (pos > 1 && pos < iterations - 2) {
                middlePart = text.substring(pos - 1 - startsub - endsub, pos - 1 - endsub);
            }

            if(pos > 0 && pos <= length) {
                endPart = text.substring(pos - 1, pos);
            }

            if(pos < length) {
                last = text.substring(pos);
            }

            if(pos >= iterations - length) {
                before = text.substring(0, pos + 1 - (iterations - length));
            }

            before = normalFormat + before;
            startPart = startFormat + startPart;
            middlePart = middleFormat + middlePart;
            endPart = endFormat + endPart;
            last = normalFormat + last;

            String frame = before + startPart + middlePart + endPart + last;
            frames.add(frame);
        }

        for (int i = 0; i < pause; i++) {
            frames.add(normalFormat + text);
        }

    }

    public String next() {
        if(frame >= frames.size())
            frame = 0;
        String s = frames.get(frame);
        frame++;
        return s;
    }



}