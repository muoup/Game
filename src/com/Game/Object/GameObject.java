package com.Game.Object;

import com.Game.Entity.Player.Player;
import com.Game.Main.Main;
import com.Game.World.World;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class GameObject {
    public static BufferedImage[] objectImages = {
            getImage("tree.png")
    };

    public Vector2 position;
    public BufferedImage image;
    public float maxTimer = 1.5f;
    protected float timer = 0;
    public int id = 0;

    public GameObject(int x, int y) {
        this.position = new Vector2(x, y);

        Main.methods.objects.add(this);
    }

    public void updateObject() {
        image = objectImages[id];

        float distance = Vector2.distance(Main.player.position, position.addClone(Render.getDimensions(image).scale(0.5f)));

        if (Render.onScreen(position, image)) {
            Render.drawImage(image,
                    position.subtractClone(World.curWorld.offset));
            update();
        }

        if (Input.GetKey(KeyEvent.VK_E)
            && distance < Render.getDimensions(image).y) {

            onInteract();
        }

        if (!Input.GetKey(KeyEvent.VK_E) || distance > Render.getDimensions(image).y)
            timer = 0;
    }

    public void update() {

    }

    public void onInteract() {

    }

    public static BufferedImage getImage(String name) {
        return Main.main.getImageFromRoot("Object/" + name);
    }

    public void drawProgressBar() {
        Vector2 sPos = position.subtractClone(image.getWidth() * -0.25f, 24).subtract(World.curWorld.offset);
        Vector2 rect = new Vector2(image.getWidth() * (timer / maxTimer) * 0.5f, 8);

        Render.setColor(Color.BLUE);
        Render.drawRectangle(sPos, rect);

        Render.setColor(Color.BLACK);
        Render.drawRectOutline(sPos, rect);
    }
}
