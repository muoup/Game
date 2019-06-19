package com.Game.Object;

import com.Game.Main.Main;
import com.Game.World.World;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class GameObject {
    public static Image[] objectImages = {
            getImage("tree.png")
    };

    public Vector2 position;
    public Image image;
    public float maxTimer = 1.5f;
    protected float timer = 0;
    public int id = 0;

    public GameObject(int x, int y) {
        this.position = new Vector2(x, y);

        Main.methods.objects.add(this);
    }

    public void updateObject() {
        timer += 1 / Main.fps;
        image = objectImages[id];

        float distance = Vector2.distance(Main.player.pos, position.addClone(Render.getDimensions(image).scale(2f)).subtractClone(World.curWorld.offset));

        if (Render.onScreen(position, image)) {
            Render.drawImage(image,
                    position.subtractClone(World.curWorld.offset));
            update();
        }

        if (Input.GetKey(KeyEvent.VK_E)
            && distance < Render.getDimensions(image).y && timer > maxTimer) {

            timer = 0;
            onInteract();
        }
    }

    public void update() {

    }

    public void onInteract() {
    }

    public static Image getImage(String name) {
        return Main.main.getImageFromRoot("Object/" + name);
    }
}
