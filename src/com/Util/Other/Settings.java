package com.Util.Other;

import com.Util.Math.Vector2;

import java.awt.*;

public class Settings {
    public static float cameraZoom = 320;
    public static int fpsCap = 60;
    public static boolean pause = false;
    public static boolean showFPS = false;
    public static int resolutionIndex = 0;
    public static final Vector2[] resolutions = {
            new Vector2(800, 600),
            new Vector2(1200, 900),
            new Vector2(1960, 1280)
    };
    public static int fontSize = 25;
    public static boolean fullScreen = false;
    public static float cameraSensitivity = 0.255f;

    public static Vector2 curResolution() {
        return Settings.resolutions[Settings.resolutionIndex].clone();
    }

    public static int sWidth(String string, Graphics g) {
        return g.getFontMetrics().stringWidth(string);
    }

}
