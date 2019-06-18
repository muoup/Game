package com.Util.Math;

public class DeltaMath {
    public static int[] fillArray(int content, int length) {
        int[] array = new int[length];

        for (int i = 0; i < array.length; i++) {
            array[i] = content;
        }

        return array;
    }
    
}
