package com.Game.Entity.Player;

import com.Game.Entity.Enemy.Enemy;
import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.GUI;
import com.Game.GUI.GUIWindow.GUILibrary;
import com.Game.GUI.GUIWindow.GUIWindow;
import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.Items.ItemStack;
import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Game.Object.GameObject;
import com.Game.Projectile.Fist;
import com.Game.World.World;
import com.Game.listener.Input;
import com.Util.Math.DeltaMath;
import com.Util.Math.Vector2;
import com.Util.Other.*;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {
    public Vector2 position;
    public int subWorld = 0;
    public float speed;
    public Color playerColor;
    public boolean canMove = true;
    public float speedMod = 0f;
    public float dashMultiplier = 0f;
    public float dashTimer = 0f;
    public Vector2 scale;
    private AnimatedSprite image;
    private Vector2 curSpeed = Vector2.zero();
    public float dx = 0, dy = 0, dMod = 0, shootTimer = 0;
    public String name = null;
    private final float maxSlide = 8f;

    public float maxHealth = 100f;
    public float health = 100f;

    public float healthRegen = 1f;

    public Player(Vector2 position, float speed, Color playerColor, float dash) {
        this.position = position;
        this.speed = speed;
        this.playerColor = playerColor;
        this.dashMultiplier = dash;
        this.scale = new Vector2(28, 76);
        this.image = new AnimatedSprite(SpriteSheet.playerSheet, 12, 0);

        init();
    }

    public void init() {
    }

    public void update() {
        shootTimer -= Main.dTime();
        dashTimer -= Main.dTime();

        if (health < maxHealth)
            health += Main.dTime() * healthRegen;

        movement();
        handleOffset();
    }

    public void damage(float amount) {
        float dmg = amount - amount * damageReduction();
        health -= dmg;
    }

    public float damageReduction() {
        float armor = AccessoriesManager.armor;
        return 0.01f * (100 - (100 / ((armor / 1000) + 1)) + DeltaMath.range(-7.5f, 7.5f));
    }

    public void movement() {
        if (speedMod > 0.1) {
            speedMod *= 0.93;
        } else if (speedMod < 0.1) {
            speedMod = 0;
        }

        dx = 0;
        dy = 0;
        curSpeed = Vector2.zero();

        if (canMove && !ChatBox.typing && Main.fps > 0) {

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

            if (Input.GetKeyDown(KeyEvent.VK_E)) {
                GameObject.checkSingleInteract();
            }

            if (Input.GetKeyDown(KeyEvent.VK_SHIFT) && dashTimer <= 0) {
                speedMod = speed * dashMultiplier;
                dashTimer = Settings.dashTimer;
            }

            if (Input.GetKey(KeyEvent.VK_SPACE) && shootTimer <= 0) {
                ItemStack accessory = AccessoriesManager.getSlot(AccessoriesManager.WEAPON_SLOT);
                if (accessory.getID() == 0)
                    new Fist(position, Input.mousePosition.addClone(World.curWorld.offset));
                else
                    accessory.getItem().useWeapon(position, Input.mousePosition.addClone(World.curWorld.offset));

                SoundHandler.playSound("default_shoot.wav");
            }

            if (Input.GetKeyDown(KeyEvent.VK_TAB)) {
                GUI.currentGUI = (GUI.currentGUI.isEmpty()) ?
                        GUILibrary.bankingGUI :
                        GUIWindow.emptyGUI;
            }

            if (dx < 0)
                image.row = 0;

            if (dx > 0)
                image.row = 1;

            dMod = speedMod;

            if (dx != 0 && dy != 0)
                dMod /= Math.sqrt(2);

            curSpeed = new Vector2((float) ((dx + Math.signum(dx) * dMod) / Main.fps), (float) ((dy + Math.signum(dy) * dMod) / Main.fps));
        }

        Vector2 movement = handleCollision(curSpeed);

        if (!movement.equalTo(Vector2.zero())) {
            position.add(movement);
            sendMovementPacket();
        }
    }

    public void tpToPos(int x, int y, int subWorld) {
        World.changeWorld(subWorld);
        position.x = x;
        position.y = y;
        sendMovementPacket();
    }

    public void sendMovementPacket() {
        Main.sendPacket("15" + Main.player.name + ":" + (int) position.x + ":" + (int) position.y + ":" + subWorld);
    }

    public Vector2[] getPoints(Vector2 offset) {
        Vector2 pos = position.addClone(offset);

        return new Vector2[]{
                pos.addClone(new Vector2(scale.x, scale.y).scale(0.5f)),
                pos.addClone(new Vector2(-scale.x, -scale.y).scale(0.5f)),
                pos.addClone(new Vector2(-scale.x, scale.y).scale(0.5f)),
                pos.addClone(new Vector2(scale.x, -scale.y).scale(0.5f))
        };
    }

    public Vector2 handleCollision(Vector2 curSpeed) {
        Vector2 speed = curSpeed;
        Vector2 points[] = getPoints(curSpeed);

        if (!CollisionHandler.isFree(points)) {
            Vector2 xPoints[] = getPoints(new Vector2(speed.x, 0));
            Vector2 yPoints[] = getPoints(new Vector2(0, speed.y));

            if (curSpeed.x != 0 && !CollisionHandler.isFree(xPoints))
                speed.x = 0;

            if (curSpeed.y != 0 && !CollisionHandler.isFree(yPoints))
                speed.y = 0;
        }

        return speed;
    }

    public void handleOffset() {
        // These variables are probably not all necessary but it looks cleaner.
        Vector2 offset = World.curWorld.offset;
        Vector2 size = World.curWorld.size;
        Vector2 res = Settings.curResolution();
        Vector2 middle = res.scaleClone(0.5f);
        Vector2 sens = middle.scaleClone(Settings.cameraSensitivity);
        Vector2 arcsens = middle.scaleClone(1 - Settings.cameraSensitivity);

        float rX = (position.x - World.curWorld.offset.x);
        float rY = (position.y - World.curWorld.offset.y);

        if (rX < middle.x - sens.x) {
            offset.x = position.x - arcsens.x;
        } else if (rX > middle.x + sens.x) {
            offset.x = position.x - sens.x - middle.x;
        }

        if (rY < middle.y - sens.y) {
            offset.y = position.y - arcsens.y;
        } else if (rY > middle.y + sens.y) {
            offset.y = position.y - sens.y - middle.y;
        }

        Vector2 maximum = size.scaleClone(Settings.worldScale).subtractClone(Settings.curResolution());

        if (offset.x < 0) {
            offset.x = 0;
        } else if (offset.x > maximum.x) {
            offset.x = maximum.x;
        }

        if (offset.y < 0) {
            offset.y = 0;
        } else if (offset.y > maximum.y) {
            offset.y = maximum.y;
        }
    }

    public void drawBar(Vector2 startPos, Color color, float current, float max) {

        Render.setColor(Color.LIGHT_GRAY);
        Render.drawRectangle(startPos,
                new Vector2(GUI.IntBoxSize * 4, 16));

        Render.setColor(color);
        Render.drawRectangle(startPos,
                new Vector2(GUI.IntBoxSize * 4 * (current / max), 16));

        Render.setColor(Color.BLACK);
        Render.drawRectOutline(startPos,
                new Vector2(GUI.IntBoxSize * 4, 16));
    }

    public void renderStats() {
        drawBar(GUI.GuiPos.subtractClone(new Vector2(0, 36)), Color.RED, health, maxHealth);
        drawBar(GUI.GuiPos.subtractClone(new Vector2(0, 18)), Color.CYAN.darker(), Settings.dashTimer - Math.max(0, dashTimer), Settings.dashTimer);

        if (health <= 0) {
            ChatBox.sendMessage("Oh no. You are dead!");

            health = maxHealth;
            position = Settings.playerSpawn.clone();
            World.changeWorld(0);
            resetAggro();
            sendMovementPacket();
        }
    }

    public void resetAggro() {
        for (Enemy enemy : MethodHandler.enemies) {
            enemy.loseTarget();
        }
    }

    public void render() {
        Render.drawImage(image.getImage(scale), position.x - scale.x / 2 - World.curWorld.offset.x,
                position.y - scale.y / 2 - World.curWorld.offset.y);

        renderStats();
    }

    public Image getImage() {
        return image.getImage();
    }
}