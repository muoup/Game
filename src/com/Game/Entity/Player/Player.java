package com.Game.Entity.Player;

import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Inventory.Item;
import com.Game.Main.Main;
import com.Game.Projectile.Bullet;
import com.Game.World.World;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;
import javafx.scene.input.KeyCode;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Player {
    public Vector2 pos;
    public float speed;
    public Color playerColor;
    public boolean canMove = true;
    public float speedMod = 0f;
    public float dashMultiplier = 0f;
    public int scale = 0;
    public BufferedImage image;
    private Vector2 curSpeed = Vector2.zero();
    private float dx = 0;
    private float dy = 0;
    private float dMod = 0;



    public Player() {
        pos = Vector2.zero();
        speed = 0;
        playerColor = Color.BLACK;

        init();
    }

    public Player(Vector2 pos, float speed, Color playerColor, float dash) {
        this.pos = pos;
        this.speed = speed;
        this.playerColor = playerColor;
        this.dashMultiplier = dash;
        this.image = Main.main.getImageFromRoot("player.png");
        this.scale = image.getWidth();

        init();
    }

    public void init() {
        InventoryManager.init();

        //InventoryManager.addItem(Item.wood, 1);
    }

    public void update() {
        movement();
        handleOffset();

        InventoryManager.update();

        if (Input.GetKeyDown(KeyEvent.VK_I)) {
            InventoryManager.addItem(Item.wood, 1);
        }
    }

    public void movement() {
        if (speedMod > 0.1) {
            speedMod *= 0.93;
        } else if (speedMod < 0.1) {
            speedMod = 0;
        }

        dx = 0;
        dy = 0;

        if (canMove && Main.fps > 0) {

            if (Input.GetKey(KeyEvent.VK_A)) {
                dx -= speed;
            }

            if (Input.GetKey(KeyEvent.VK_D)) {
                dx += speed;
            }

            if (Input.GetKey(KeyEvent.VK_W)) {
                dy -= speed;
            }

            if (Input.GetKey(KeyEvent.VK_S)) {
                dy += speed;
            }

            if (Input.GetKeyDown(KeyEvent.VK_SHIFT)) {
                speedMod = speed * dashMultiplier;
            }

            if (Input.GetKeyDown(KeyEvent.VK_SPACE)) {
                new Bullet(pos.subtractClone(World.curWorld.offset), Input.mousePosition, new Vector2(8, 8), 2, 4);
            }

            dMod = speedMod;

            if (dx != 0 && dy != 0)
                dMod /= Math.sqrt(2);

            curSpeed = new Vector2((float) ((dx + Math.signum(dx) * dMod) / Main.fps), (float) ((dy + Math.signum(dy) * dMod) / Main.fps));
        }

        pos.add(curSpeed);
    }

    public void handleOffset() {
        Vector2 offset = World.curWorld.offset;
        Vector2 size = World.curWorld.size;
        Vector2 res = Settings.curResolution();
        Vector2 middle = res.scaleClone(0.5f);
        Vector2 sens = middle.scaleClone(Settings.cameraSensitivity);
        Vector2 arcsens = middle.scaleClone(1 - Settings.cameraSensitivity);

        float rX = (pos.x - World.curWorld.offset.x);
        float rY = (pos.y - World.curWorld.offset.y);

        if (rX < middle.x - sens.x) {
            offset.x = pos.x - arcsens.x;
        } else if (rX > middle.x + sens.x) {
            offset.x = pos.x - sens.x - middle.x;
        }

        if (rY < middle.y - sens.y) {
            offset.y = pos.y - arcsens.y;
        } else if (rY > middle.y + sens.y) {
            offset.y = pos.y - sens.y - middle.y;
        }

        if (offset.x < 0) {
            offset.x = 0;
        } else if (offset.x > size.x - Settings.curResolution().x) {
            offset.x = size.x - Settings.curResolution().x;
        }

        if (offset.y < 0) {
            offset.y = 0;
        } else if (offset.y > size.y - Settings.curResolution().y) {
            offset.y = size.y - Settings.curResolution().y;
        }
}

    public void render() {
        Render.drawImage(image, pos.x - scale / 2 - World.curWorld.offset.x,
                pos.y - scale / 2 - World.curWorld.offset.y);

        InventoryManager.render();
    }
}
