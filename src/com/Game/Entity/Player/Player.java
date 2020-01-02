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
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Player {
    public Vector2 position;
    public int subWorld = 0;
    public float speed;
    public Color playerColor;
    public boolean canMove = true;
    public float speedMod = 0f;
    public float dashMultiplier = 0f;
    public float dashTimer = 0f;
    public int scale = 0;
    public BufferedImage image;
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
        this.image = Main.main.getImageFromRoot("player.png");
        this.scale = image.getWidth();

        init();
    }

    public void init() {
    }

    public Vector2[] getPoints(Vector2 offset) {
        Vector2 pos = position.addClone(offset);
        Vector2[] points = new Vector2[4];
        points[0] = pos.addClone(scale / 2, scale / 2);
        points[1] = pos.addClone(-scale / 2, -scale / 2);
        points[2] = pos.addClone(-scale / 2, scale / 2);
        points[3] = pos.addClone(scale / 2, -scale / 2);

        return points;
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
            }

            if (Input.GetKeyDown(KeyEvent.VK_TAB)) {
                GUI.currentGUI = (GUI.currentGUI.isEmpty()) ?
                        GUILibrary.bankingGUI :
                        GUIWindow.emptyGUI;
            }

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

    public void test() {
//        Vector2 aim = Input.mousePosition.addClone(World.curWorld.offset);
//
//        double radius = 128;
//        double theta = Math.atan((aim.x - position.x) / (aim.y - position.y));
//
//        if (aim.y - position.y <= 0) {
//            theta += DeltaMath.pi;
//        }
//
//        Vector2 newAim = position.addClone(radius * Math.sin(theta),radius * Math.cos(theta));
//        Vector2 newAim2 = position.addClone(radius * Math.sin(theta + DeltaMath.pi / 8),radius * Math.cos(theta + DeltaMath.pi / 8));
//        Vector2 newAim3 = position.addClone(radius * Math.sin(theta - DeltaMath.pi / 8),radius * Math.cos(theta - DeltaMath.pi / 8));
//
//        Render.drawLine(position.subtractClone(World.curWorld.offset), newAim.subtractClone(World.curWorld.offset));
//        Render.drawLine(position.subtractClone(World.curWorld.offset), newAim2.subtractClone(World.curWorld.offset));
//        Render.drawLine(position.subtractClone(World.curWorld.offset), newAim3.subtractClone(World.curWorld.offset));
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

    public Vector2 handleCollision(Vector2 curSpeed) {
        Vector2 speed = curSpeed.clone();
        Vector2[] xPoints = getPoints(new Vector2(curSpeed.x, 0));
        Vector2[] x1Points = {
            xPoints[1],
            xPoints[2]
        };
        Vector2[] x2Points = {
            xPoints[0],
            xPoints[3]
        };
        Vector2[] yPoints = getPoints(new Vector2(0, curSpeed.y));
        Vector2[] y1Points = {
            yPoints[1],
            yPoints[3]
        };
        Vector2[] y2Points = {
                yPoints[0],
                yPoints[2]
        };

        if ((speed.x < 0 && !CollisionHandler.isFree(x1Points)) || (speed.x > 0 && !CollisionHandler.isFree(x2Points))) {
            speed.x = 0;
        }

        if ((speed.y > 0 && !CollisionHandler.isFree(y2Points)) || (speed.y < 0 && !CollisionHandler.isFree(y1Points))) {
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
        Render.drawImage(image, position.x - scale / 2 - World.curWorld.offset.x,
                position.y - scale / 2 - World.curWorld.offset.y);

        renderStats();

        test();
    }
}