package com.Game.Networking;

import com.Game.Entity.Player;
import com.Game.Main.MethodHandler;
import com.Game.World.World;
import com.Util.Math.Vector2;
import com.Util.Other.AnimatedSprite;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayerObject {
    private int x, y;
    private String username;
    private float nameOffset;
    private AnimatedSprite animation;
    private boolean facingLeft = false;

    public PlayerObject(int x, int y, final String username) {
        this.x = x;
        this.y = y;
        this.username = username;
        Render.setFont(Settings.npcFont);
        this.nameOffset = 0.5f * Render.getStringWidth(username);
        animation = Player.idleAnimation;
    }

    public static void animate(String username, String animation) {
        for (PlayerObject player : MethodHandler.playerConnections) {
            if (player.username.equals(username)) {
                if (player.getAnimation().equals(animation))
                    return;

                player.setAnimation(animation);
            }
        }
    }

    public void setAnimation(String animation) {
        for (AnimatedSprite sprite : Player.animationList) {
            if (sprite.getName().equals(animation)) {
                this.animation = sprite.createNewInstance();
                return;
            }
        }
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;

        Player.handleOffset();
    }

    public Vector2 getPos() {
        return new Vector2(x, y);
    }

    public String getUsername() {
        return username;
    }

    public void render() {
        BufferedImage image = Render.getScaledImage(this.animation.getImage(), Player.scale);

        if (Render.onScreen(getPos(), image)) {
            Render.setFont(Settings.npcFont);
            Vector2 drawPos = new Vector2(getPos().x - World.offset.x,
                    getPos().y - World.offset.y);
            Render.drawImage((!facingLeft) ? image : Render.mirrorImageHorizontally(image), drawPos.subtractClone(Player.scale.x / 2, Player.scale.y / 2));
            Render.setColor(Color.BLACK);
            Render.drawText(username, drawPos.subtractClone(nameOffset, 24));
        }
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    public String getAnimation() {
        return animation.getName();
    }
}
