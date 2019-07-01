package com.Game.Entity.Player;

import com.Game.World.World;
import com.Util.Math.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CollisionHandler {

    private static Color colColor = new Color(0, 0, 0);

    public static boolean isFree(Vector2 position) {
        BufferedImage col = World.curWorld.colWorld;

        if (position.x > col.getWidth() || position.y > col.getHeight())
            return false;

        if (position.x < 0 || position.y < 0)
            return false;

        int rgb = col.getRGB((int) position.x, (int) position.y);

        return colColor.getRGB() != rgb;
    }

    public static boolean isFree(Vector2[] positions) {
        BufferedImage col = World.curWorld.colWorld;

        for (Vector2 p : positions) {
            if (p.x > col.getWidth() || p.y > col.getHeight())
                return false;

            if (p.x < 0 || p.y < 0)
                return false;

            int rgb = col.getRGB((int) p.x, (int) p.y);

            if (rgb == colColor.getRGB())
                return false;
        }

        return true;
    }
}
