package com.Util.Math;

public class DeltaMath {
    public static int[] fillArray(int content, int length) {
        int[] array = new int[length];

        for (int i = 0; i < array.length; i++) {
            array[i] = content;
        }

        return array;
    }

    public static float roundTo(float in, float to) {
        float dIn = in / to;
        dIn = Math.round(dIn);
        return dIn * to;
    }

    public static float logb(float base, float number) {
        return (float) (Math.log10(number) / Math.log10(base));
    }
}
