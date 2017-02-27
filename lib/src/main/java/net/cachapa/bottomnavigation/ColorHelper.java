package net.cachapa.bottomnavigation;

import android.graphics.Color;

public class ColorHelper {
    /**
     * Lightens a color by a given ammount
     *
     * @param amount a value between 0 and 1
     */
    public static int lightenColor(int color, float amount) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);

        hsv[2] = hsv[2] * (1 + amount);
        return Color.HSVToColor(hsv);
    }
}
