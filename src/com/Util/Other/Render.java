package com.Util.Other;

import com.Game.Main.Main;
import com.Util.Math.Vector2;

import java.awt.*;

public class Render {
    public static void drawImage(Image image, Vector2 position) {
        Main.graphics.drawImage(image, (int) position.x, (int) position.y, null);
    }

    public static void drawImage(Image image, float x, float y) {
        Main.graphics.drawImage(image, (int) x, (int) y, null);
    }

    public static void drawRectangle(Vector2 position, Vector2 scale) {
        Main.graphics.fillRect((int) position.x, (int) position.y, (int) scale.x, (int) scale.y);
    }

    public static void drawRectangle(float x, float y, float width, float height) {
        Main.graphics.fillRect((int) x, (int) y, (int) width, (int) height);
    }

    public static void setColor(Color color) {
        Main.graphics.setColor(color);
    }

    public static void setFont(Font font) {
        Main.graphics.setFont(font);
    }

    public static void drawText(String text, Vector2 position) {
        Main.graphics.drawString(text, (int) position.x, (int) position.y);
    }

    public static void drawText(String text, float x, float y) {
        Main.graphics.drawString(text, (int) x, (int) y);
    }

    public static Vector2 getDimensions(Image image) {
        return new Vector2(image.getWidth(null), image.getHeight(null));
    }
}
