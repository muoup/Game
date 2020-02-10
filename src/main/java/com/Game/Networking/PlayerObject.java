package com.Game.Networking;

import com.Game.Entity.Player.Player;
import com.Game.Main.Main;
import com.Game.World.World;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayerObject {
    private int x, y, subWorld;
    private String username;
    private float nameOffset;
    private BufferedImage image;

    public PlayerObject(int x, int y, final String username) {
        this.x = x;
        this.y = y;
        this.username = username;
        Render.setFont(Settings.npcFont);
        this.nameOffset = 0.5f * Render.getStringWidth(username);
        setImage(Player.idleAnimation.getFrame(0));
    }

    public void setPos(int x, int y, int subWorld) {
        this.x = x;
        this.y = y;
        this.subWorld = subWorld;
    }

    public Vector2 getPos() {
        return new Vector2(x, y);
    }

    public String getUsername() {
        return username;
    }

    public void tick() {
        if (Render.onScreen(getPos(), image) && Main.player.subWorld == subWorld) {
            Render.setFont(Settings.npcFont);
            Vector2 drawPos = new Vector2(getPos().x - World.curWorld.offset.x,
                    getPos().y - World.curWorld.offset.y);
            Render.drawImage(image, drawPos.subtractClone(Main.player.scale.x / 2, Main.player.scale.y / 2));
            Render.setColor(Color.BLACK);
            Render.drawText(username, drawPos.subtractClone(nameOffset, 24));
        }
    }

    public void setImage(BufferedImage image) {
        this.image = Render.getScaledImage(image, Main.player.scale);
    }
}
