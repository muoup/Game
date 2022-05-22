package com.Util.Other;

import com.Game.Main.Main;
import com.Game.Main.MenuHandler;
import com.Util.Math.Vector2;

import java.awt.*;
import java.util.TimerTask;

public class Settings {
    public static float cameraZoom = 320;
    public static int fpsCap = 60;
    public static boolean showFPS = false;
    public static int resolutionIndex = 1;
    public static final float maxPickupDistance = 128;
    public static int worldScale = 2;

    // In case xp rates are too fast, I'll cap leveling
    // This'll increase over time when more content is released
    public static final Vector2[] resolutions = {
            new Vector2(800, 600),
            new Vector2(1200, 900),
            new Vector2(1960, 1280)
    };

    public static final double scrollSpeed = 85f;

    public static Font npcFont = null;
    public static Font skillPopupFont = null;
    public static Font itemFont = null;
    public static Font questFont = null;
    public static Font groundFont = null;

    public static int fontSize = 25;
    public static int volume = 0;
    public static boolean fullScreen = false;
    public static float cameraSensitivity = 0.25f;

    public static int customAmount = 1000;

    public static Vector2 playerSpawn = new Vector2(1000, 5500);
    public static final float projLengthMultiplier = 1.775f;
    public static final float dashTimer = 0.75f;

    public static int questCount = 0;
    public static float playerSpeed = 225f;
    public static boolean inStack = true;

    public static Vector2 curResolution() {
        return Settings.resolutions[Settings.resolutionIndex].clone();
    }

    public static int sWidth(String string) {
        return Main.graphics.getFontMetrics().stringWidth(string);
    }

    public static boolean paused() {
        return Main.menu.state != MenuHandler.MenuState.NoPause;
    }

    public static void disablePause() {
        MenuHandler.setState(MenuHandler.MenuState.NoPause);
    }

    public static TimerTask wrap(Runnable r) {
        return new TimerTask() {

            @Override
            public void run() {
                r.run();
            }
        };
    }

    public static float sHeight() {
        return Main.graphics.getFontMetrics().getHeight();
    }
}
