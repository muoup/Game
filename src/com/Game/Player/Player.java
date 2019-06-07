package com.Game.Player;

import com.Game.Main.Main;
import com.Game.Projectile.Bullet;
import com.Game.World.World;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Settings;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {
    public Vector2 pos;
    public float speed;
    public Color playerColor;
    public boolean canMove = true;
    public float speedMod = 0f;
    public float dashMultiplier = 0f;
    public int scale = 0;
    private Vector2 curSpeed = Vector2.zero();
    private float dx = 0;
    private float dy = 0;
    private float dMod = 0;

    public Player() {
        pos = Vector2.zero();
        speed = 0;
        playerColor = Color.BLACK;
    }

    public Player(Vector2 pos, float speed, Color playerColor, float dash, int scale) {
        this.pos = pos;
        this.speed = speed;
        this.playerColor = playerColor;
        this.dashMultiplier = dash;
        this.scale = 40;
    }

    public void update() {
        movement();
        handleOffset();
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
                new Bullet(pos, Input.mousePosition, new Vector2(8, 8), 2, 4);
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

        Vector2 middle = res.scale(0.5f);

        float difX = pos.x - World.curWorld.offset.x;
        float difY = pos.y - World.curWorld.offset.y;

        Vector2 compare = new Vector2(middle.x + Settings.cameraSensitivity * res.x,
                middle.y + Settings.cameraSensitivity * res.y);

        if (Math.abs(difX) > compare.x) {
            offset.x = pos.x - compare.x * Math.signum(difX);
        }

        if (Math.abs(difY) > compare.y) {
            offset.y = pos.y - compare.y * Math.signum(difY);
        }

        if (offset.x < 0) {
            offset.x = 0;
        } else if (offset.x > size.x - (1 - Settings.cameraSensitivity) * Settings.curResolution().y) {
            offset.x = size.x - Settings.curResolution().x;
        }

        if (offset.y < 0) {
            offset.y = 0;
        } else if (offset.y > size.y - (1 - Settings.cameraSensitivity) * Settings.curResolution().x) {
            offset.y = size.y - Settings.curResolution().y;
        }
    }

    public void render(Graphics g) {
        g.setColor(playerColor);
        g.fillRect((int) pos.x - scale / 2 - (int) World.curWorld.offset.x, (int) pos.y - scale / 2 - (int) World.curWorld.offset.y, scale, scale);
    }
}
