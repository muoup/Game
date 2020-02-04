package com.Game.Entity.Player;

import com.Game.Entity.Enemy.Enemy;
import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.GUI;
import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.GUI.RightClick;
import com.Game.GUI.Shop.Shop;
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

public class Player {//constructor for the Player class
    public Vector2 position;//creates the Vector for the player position
    public int subWorld = 0;//indicates the subworld the player is in
    public float speed;//creates the speed variable
    public Color playerColor;//
    public boolean canMove = true;//indicates the player can move
    public float speedMod = 0f;//modifier of the player's speed (for future buffs and debuffs)
    public float dashMultiplier = 0f;//dash speed
    public float dashTimer = 0f;//dash timer
    public Vector2 scale;//size of the player sprite for render
    private Vector2 curSpeed = Vector2.zero();
    public float dx = 0, dy = 0, dMod = 0, shootTimer = 0;
    public String name = null;//creating the player name and getting from a save file later
    private final float maxSlide = 8f;

    public float maxHealth = 100f;//sets max health
    public float health = 100f;//sets current health

    public float healthRegen = 1f;//sets how quickly health regenerates

    public static final SpriteSheet playerIdle = new SpriteSheet("/Player/player_idle_1.png", 32, 32);//setting the playerIdle sprite (in src/res/images)
    public static final SpriteSheet playerRun = new SpriteSheet("/Player/player_run.png", 32, 32);//setting the playerRun sprite
    public static final SpriteSheet playerChop  = new SpriteSheet("/Player/player_chop.png", 32, 32);//setting the playerChop sprite

    public static final AnimatedSprite idleAnimation = new AnimatedSprite(8, playerIdle, 2);//using the associated spritesheet to create an animation of 8 frames for 2 seconds
    public static final AnimatedSprite runAnimation = new AnimatedSprite(12, playerRun, 4);//using the associated spritesheet to create an animation of 12 frames for 4 seconds
    public static final AnimatedSprite chopAnimation = new AnimatedSprite(16, playerChop, 8);//using the associated spritesheet to create an animation of 16 frames for 8 seconds

    public AnimatedSprite current = idleAnimation;//sets the current animation that is playing
    private boolean leftFacing = true;//bool for checking if the player is left facing (to implement reversing the direction of the player

    public Player(Vector2 position, float speed, Color playerColor, float dash) {
        this.position = position;
        this.speed = speed;
        this.playerColor = playerColor;
        this.dashMultiplier = dash;
        this.scale = new Vector2(80, 80);

        init();
    }

    public void init() {
    }

    public void update() {
        shootTimer -= Main.dTime();
        dashTimer -= Main.dTime();

        current.update();

        if (health < maxHealth)
            health += Main.dTime() * healthRegen;

        if (!Settings.paused())
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
                curSpeed.x -= 1;
            }

            if (Input.GetKey(KeyEvent.VK_D)) {
                curSpeed.x += 1;
            }

            if (Input.GetKey(KeyEvent.VK_W)) {
                curSpeed.y -= 1;
            }

            if (Input.GetKey(KeyEvent.VK_S)) {
                curSpeed.y += 1;
            }

            if (Input.GetKeyDown(KeyEvent.VK_E)) {
                GameObject.checkSingleInteract();
            }

            if (Input.GetKeyDown(KeyEvent.VK_SHIFT) && dashTimer <= 0) {
                speedMod = dashMultiplier;
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
        }

        Vector2 move = curSpeed.normalize().scaleClone((float) Main.dTime() * speed * (1 + speedMod));
        Vector2 movement = handleCollision(move);

        if (!movement.equalTo(Vector2.zero())) {
            position.add(movement);
            GUI.currentShop = Shop.empty;
            changeSprite(runAnimation);
            sendMovementPacket();

            if (movement.x > 0) leftFacing = false; else leftFacing = true;
        } else if (current == runAnimation){
            changeSprite(idleAnimation);
        }
    }

    public void changeSprite(AnimatedSprite spriteSheet) {
        if (current != spriteSheet) {
            current = spriteSheet;
            current.reset();
        }
    }

    public void tpToPos(int x, int y, int subWorld) {
        World.changeWorld(subWorld);
        position.x = x;
        position.y = y;
        sendMovementPacket();
    }

    public void sendMovementPacket() {
        cancelCurrent();
        Main.sendPacket("15" + Main.player.name + ":" + (int) position.x + ":" + (int) position.y + ":" + subWorld);
    }

    private void cancelCurrent() {
        RightClick.object.loseFocus();
    }

    public Vector2[] getPoints(Vector2 offset) {//gets the four corners of the player for collision 
        Vector2 pos = position.addClone(offset);

        return new Vector2[]{
                pos.addClone(new Vector2(scale.x, scale.y).scale(0.5f)),
                pos.addClone(new Vector2(-scale.x, -scale.y).scale(0.5f)),
                pos.addClone(new Vector2(-scale.x, scale.y).scale(0.5f)),
                pos.addClone(new Vector2(scale.x, -scale.y).scale(0.5f))
        };
    }

    public Vector2 handleCollision(Vector2 curSpeed) {//handles collision with solid objects
        Vector2 speed = curSpeed.clone();
        Vector2[] points = getPoints(curSpeed);

        if (!CollisionHandler.isFree(points)) {
            Vector2[] xPoints = getPoints(new Vector2(speed.x, 0));
            Vector2[] yPoints = getPoints(new Vector2(0, speed.y));

            if (curSpeed.x != 0 && !CollisionHandler.isFree(xPoints))
                speed.x = 0;

            if (curSpeed.y != 0 && !CollisionHandler.isFree(yPoints))
                speed.y = 0;
        }

        return speed;
    }

    public void handleOffset() {//determines the offset of the camera so the camera can stop following the player when they run into the border of the map
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

    public void drawBar(Vector2 startPos, Color color, float current, float max) {//draws the bar that renderStats() renders the health and dash bars into
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

    public void renderStats() {//Renders the health bar and dash bar onto the screen above the menu. Only does the red and the cyan bars, does not do the outline.
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

    public void resetAggro() {//resets enemy aggression at a certain range
        for (Enemy enemy : MethodHandler.enemies) {
            enemy.loseTarget();
        }
    }

    public void render() { //render draws the image on the screen at the proper coordinates and scale
        Render.drawImage(getImage(), position.x - scale.x / 2 - World.curWorld.offset.x,
                position.y - scale.y / 2 - World.curWorld.offset.y);

        renderStats();
    }

    public Image getImage() {//gets the current animation state for the player sprite
        return current.getImage(scale);
    }
}
