package com.Util.Other;

import com.Game.Main.Main;
import com.Util.Math.Vector2;

import java.awt.*;

public class Settings {
    public static float cameraZoom = 320;
    public static int fpsCap = 60;
    public static boolean pause = false;
    public static boolean showFPS = false;
    public static int resolutionIndex = 1;
    public static final float maxDistance = 50;
    public static Font itemFont;
    public static int worldScale = 2;
    // In case xp rates are too fast, I'll cap leveling
    // This'll increase over time when more content is released
    public static final int lvlMax = 50;
    public static float dashTimer = .75f;
    public static final Vector2[] resolutions = {
            new Vector2(800, 600),
            new Vector2(1200, 900),
            new Vector2(1960, 1280)
    };

    // Change these to make xp faster.
    public static final float rangedXPMultiplier = 1.5f;
    public static final float meleeXPMultiplier = 1.65f;

    public static Font npcFont = null;
    public static Font skillPopupFont = null;

    public static int fontSize = 25;
    public static boolean fullScreen = false;
    public static float cameraSensitivity = 0.25f;

    public static Vector2 playerSpawn = new Vector2(1000, 5500);

    public static Vector2 curResolution() {
        return Settings.resolutions[Settings.resolutionIndex].clone();
    }

    public static int sWidth(String string) {
        return Main.graphics.getFontMetrics().stringWidth(string);
    }

}
