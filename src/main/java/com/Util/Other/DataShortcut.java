package com.Util.Other;

public class DataShortcut {
    public static boolean multiCompare(Object a, Object... comparison) {
        for (Object b : comparison) {
            if (a.equals(b)) {
                return true;
            }
        }

        return false;
    }
}
