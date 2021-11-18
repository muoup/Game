package com.Game.Entity;

import com.Game.World.World;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CollisionHandler {

    private static Color colColor = new Color(0, 0, 0);

    public static boolean isFree(Vector2 position) {
        BufferedImage col = World.colWorld;

        if (col == null)
            return false;

        Render.drawRectangle(position, new Vector2(8));

        if (position.x > col.getWidth() * Settings.worldScale || position.y > col.getHeight() * Settings.worldScale)
            return false;

        if (position.x < 0 || position.y < 0)
            return false;

        int rgb = col.getRGB((int) position.x / Settings.worldScale, (int) position.y / Settings.worldScale);

        return colColor.getRGB() != rgb;
    }

    public static boolean isFree(Vector2[] positions) {
        BufferedImage col = World.colWorld;

        if (col == null)
            return false;

        for (Vector2 p : positions) {
            if (!isFree(p))
                return false;
        }

        return true;
    }
}
