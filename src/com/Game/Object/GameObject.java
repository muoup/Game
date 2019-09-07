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
    public Vector2 position;
    public BufferedImage image;
    public float maxTimer = 1.5f;
    protected float timer = 0;
    protected float maxDistance;
    protected boolean canInteract = true;

    public GameObject(int x, int y) {
        this.position = new Vector2(x, y);

        Main.methods.objects.add(this);
    }

    public void updateObject() {

        float distance = Vector2.distance(Main.player.position, position);

        if (image != null) {
            if (Render.onScreen(position, image)) {
                Render.drawImage(image,
                        position.subtractClone(World.curWorld.offset));
                update();
            }
        }

        if (Input.GetKey(KeyEvent.VK_E)
            && distance < maxDistance
            && canInteract) {

            canInteract = onInteract();
        }

        if (!Input.GetKey(KeyEvent.VK_E) || distance > maxDistance) {
            timer = 0;
            canInteract = true;
        }
    }

    public void update() {

    }

    public boolean onInteract() {
        return false;
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

    public void drawPlayerProgressBar() {
        Vector2 sPos = Main.player.position.subtractClone(24, 50).subtract(World.curWorld.offset);
        Vector2 rect = new Vector2(48 * (timer / maxTimer), 8);
        Vector2 compRect = new Vector2(48, 8);

        Render.setColor(Color.LIGHT_GRAY);
        Render.drawRectangle(sPos, compRect);

        Render.setColor(Color.BLACK);
        Render.drawRectOutline(sPos, compRect);

        Render.setColor(Color.BLUE);
        Render.drawRectangle(sPos, rect);


    }
}
