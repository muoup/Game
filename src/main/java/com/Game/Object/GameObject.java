package com.Game.Object;

import com.Game.Entity.Player;
import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Game.World.World;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Sprite;

import java.awt.image.BufferedImage;

public class GameObject {
    public Vector2 position;
    public BufferedImage image;
    protected Vector2 scale;

    public GameObject(Vector2 position, Sprite image) {
        this.position = position;
        this.image = image.getImage();
        this.scale = new Vector2(this.image.getWidth(), this.image.getHeight());

        MethodHandler.objects.add(this);
    }

    public static UsableGameObject mouseOver() {
        for (GameObject obj : MethodHandler.objects) {
            if (obj.position == null || obj.image == null)
                continue;
            if (Vector2.distance(obj.position.addClone(Render.getImageSize(obj.image).scale(0.5f)), Input.mousePosition.addClone(World.offset))
                    < Math.max(obj.image.getWidth(), obj.image.getHeight())
                    && obj instanceof UsableGameObject) {
                return (UsableGameObject) obj;
            }
        }

        return null;
    }

    public static void interact() {
        Main.sendPacket("oi" + Player.name);
    }

    public static void Player() {
        Player.interactionStart = 0;
        Player.interactionFinish = 0;
        Main.sendPacket("lf" + Player.name);
    }

    public void renderObject() {
        if (image == null || !Render.onScreen(position, image))
            return;

        if (scale == null)
            scale = Render.getImageSize(image);

        Render.drawImage(Render.getScaledImage(image, scale),
                position.subtractClone(World.offset).subtractClone(scale.scaleClone(0.5f)));
    }

    public static BufferedImage getImage(String name) {
        return Main.main.getImageFromRoot("Objects/" + name);
    }

//    public void drawProgressBar() {
//        Vector2 sPos = position.subtractClone(image.getWidth() * -0.25f, 24).subtract(World.offset);
//        Vector2 rect = new Vector2(image.getWidth() * (timer / maxTimer) * 0.5f, 8);
//
//        Render.setColor(Color.BLUE);
//        Render.drawRectangle(sPos, rect);
//
//        Render.setColor(Color.BLACK);
//        Render.drawRectOutline(sPos, rect);
//    }

    public void setScale(int x, int y) {
        this.scale = new Vector2(x, y);
    }
}
