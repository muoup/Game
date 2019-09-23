package com.Game.Networking;

import com.Game.Main.Main;
import com.Game.World.World;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;

public class PlayerObject {
    private int x, y;
    private String username;
    private float nameOffset;

    public PlayerObject(int x, int y, final String username) {
        this.x = x;
        this.y = y;
        this.username = username;
        Render.setFont(Settings.npcFont);
        this.nameOffset = 0.5f * Render.getStringWidth(username);
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

    public void tick() {
        if (Render.onScreen(getPos(), Main.player.image)) {
            Render.setFont(Settings.npcFont);
            Vector2 drawPos = new Vector2(getPos().x - World.curWorld.offset.x,
                    getPos().y - World.curWorld.offset.y);
            Render.drawImage(Main.player.image, drawPos.offsetClone(-Main.player.scale / 2));
            Render.setColor(Color.BLACK);
            Render.drawText(username, drawPos.subtractClone(nameOffset, 48));
        }
    }
}
