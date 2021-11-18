package com.Game.Networking;

import com.Game.Entity.Player;
import com.Game.Main.Main;
import com.Game.World.World;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayerObject {
    private int x, y;
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

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 getPos() {
        return new Vector2(x, y);
    }

    public String getUsername() {
        return username;
    }

    public void render() {
        if (Render.onScreen(getPos(), image)) {
            Render.setFont(Settings.npcFont);
            Vector2 drawPos = new Vector2(getPos().x - World.offset.x,
                    getPos().y - World.offset.y);
            Render.drawImage(image, drawPos.subtractClone(Player.scale.x / 2, Player.scale.y / 2));
            Render.setColor(Color.BLACK);
            Render.drawText(username, drawPos.subtractClone(nameOffset, 24));
        }
    }

    public void setImage(BufferedImage image) {
        this.image = Render.getScaledImage(image, Player.scale);
    }
}
