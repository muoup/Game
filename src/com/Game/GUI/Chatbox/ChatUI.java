package com.Game.GUI.Chatbox;

import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

public class ChatUI extends ChatBox {
    private static Vector2 uiSize = Vector2.zero();

    public static void init() {
        uiSize = new Vector2(gSize.x, gSize.y / 6);
    }

    public static void render() {
        Vector2 pos = new Vector2(0, Settings.curResolution().y - gSize.y - uiSize.y);

        //Render.setColor();
        Render.drawBorderedRect(pos, uiSize);
    }

    public static void update() {

    }
}
