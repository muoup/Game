package com.Util.Math;

import java.text.DecimalFormat;

public class DeltaMath {
    public static final float pi = 3.1415926538f;

    private static final DecimalFormat format = new DecimalFormat("#,###.##");

    public static int[] fillArray(int content, int length) {
        int[] array = new int[length];

        for (int i = 0; i < array.length; i++) {
            array[i] = content;
        }

        return array;
    }

    public static boolean between(float c, float a, float b) {
        return c >= a && c <= b;
    }

    public static float roundTo(float in, float to) {
        float dIn = in / to;
        dIn = Math.round(dIn);
        return dIn * to;
    }

    public static float minpositive(float in, float t) {
        return Math.max(Math.min(t, in), 0);
    }

    public static float logb(float base, float number) {
        return (float) (Math.log10(number) / Math.log10(base));
    }

    public static float range(float low, float high) {
        return (float) Math.random() * (high - low) + low;
    }

    public static double trueArcSine(float amt) {
        return Math.asin(amt) + Math.ceil((2 * amt - pi) / pi);
    }

    public static String addCommas(float number) {
        return format.format(number);
    }
}
