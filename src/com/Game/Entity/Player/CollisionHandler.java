package com.Game.Entity.Player;

import com.Game.World.World;
import com.Util.Math.Vector2;
import com.Util.Other.Settings;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CollisionHandler {

    private static Color colColor = new Color(0, 0, 0);

    public static boolean isFree(Vector2 position) {
        World col = World.curWorld;

        if (position.x > col.getWidth() || position.y > col.getHeight())
            return false;

        if (position.x < 0 || position.y < 0)
            return false;

        int rgb = col.colWorld.getRGB((int) position.x, (int) position.y);

        return colColor.getRGB() != rgb;
    }

    public static boolean isFree(Vector2[] positions) {
        World col = World.curWorld;

        for (Vector2 p : positions) {
            if (p.x > col.getWidth() || p.y > col.getHeight())
                return false;

            if (p.x < 0 || p.y < 0)
                return false;

            int rgb = col.colWorld.getRGB((int) p.x / Settings.worldScale, (int) p.y / Settings.worldScale);

            if (rgb == colColor.getRGB())
                return false;
        }

        return true;
    }
}
