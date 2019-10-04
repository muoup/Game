package com.Game.GUI;

import com.Game.Entity.Enemy.Enemy;
import com.Game.GUI.Skills.Skills;
import com.Game.Main.MethodHandler;
import com.Game.World.World;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;

public class MouseHover {
    private static int statHover = -1;
    private static Vector2 draw = Vector2.zero();
    private static Enemy hoverEntity;

    public static void init() {

    }

    public static void handleHover(int index) {
        if (index == 2) {
            update();
            render();
        }

        updateEntity();
        renderEntity();
    }

    public static void update() {
        if (GUI.inGUI()) {
            Vector2 deltaMouse = Input.mousePosition.subtractClone(GUI.GuiPos);

            int dx = (int) deltaMouse.x / GUI.IntBoxSize;
            int dy = (int) deltaMouse.y / GUI.IntBoxSize;

            statHover = dx + dy * 4;
            draw = deltaMouse.clone();

            if (statHover >= Skills.skillAmt) {
                statHover = -1;
                draw = Vector2.zero();
            }
        }
    }

    public static void render() {
        if (statHover == -1)
            return;

        String xp = "Current XP: " + (int) Skills.getExperience(statHover);
        String lvlUp = "XP for Next Level: " + Skills.levelToExp(Skills.getLevel(statHover) + 1);

        Render.setFont(new Font("Arial", Font.BOLD, 14));

        float width = Settings.sWidth(lvlUp);
        float x = (Input.mousePosition.x + width * 1.25f > Settings.curResolution().x) ?
                Input.mousePosition.x - width * 1.1f : Input.mousePosition.x;
        Vector2 dPos = new Vector2(x, Input.mousePosition.y);

        Render.setColor(Color.BLACK);
        Render.drawRectangle(dPos, new Vector2(width * 1.1f, Settings.curResolution().y * 0.07f));

        Render.setColor(Color.GRAY);
        Render.drawRectangle(dPos.addClone(2, 2),
                new Vector2(width * 1.1f - 4, Settings.curResolution().y * 0.07f - 4));

        Render.setColor(Color.BLACK);
        Render.drawText(Skills.skillNames[statHover], dPos.addClone(width * 0.05f, Settings.curResolution().y * 0.02f));
        Render.drawText(xp, dPos.addClone(width * 0.05f, Settings.curResolution().y * 0.04f));
        Render.drawText(lvlUp, dPos.addClone(width * 0.05f, Settings.curResolution().y * 0.06f));
    }

    public static void updateEntity() {
         if (!GUI.inGUI()) {
            statHover = -1;
            draw = Vector2.zero();
            hoverEntity = null;
            for (Enemy e : MethodHandler.enemies) {
                if (e.image == null)
                    return;

                if (!Render.onScreen(e.position, e.image) || !e.enabled)
                    continue;
                if (Vector2.distance(Input.mousePosition, e.position.subtractClone(World.curWorld.offset)) <= e.image.getHeight()) {
                    draw = Input.mousePosition;
                    hoverEntity = e;
                    return;
                }
            }
        } else if (GUI.inGUI())
            hoverEntity = null;
    }

    public static void renderEntity() {
        if (hoverEntity != null) {
            float width = Settings.sWidth(hoverEntity.name);
            float x = (Input.mousePosition.x + width * 1.25f > Settings.curResolution().x) ?
                    Input.mousePosition.x - width * 1.1f : Input.mousePosition.x;
            float height = Render.getStringHeight() * 1.1f;
            Vector2 dPos = new Vector2(x, Input.mousePosition.y);

            Render.setColor(Color.BLACK);
            Render.drawRectangle(dPos, new Vector2(width * 1.1f, height));

            Render.setColor(Color.LIGHT_GRAY);
            Render.drawRectangle(dPos.addClone(2, 2),
                    new Vector2(width * 1.1f - 4, height - 4));

            Render.setColor(Color.BLACK);
            Render.drawText(hoverEntity.name, dPos.addClone(width * 0.05f, Settings.curResolution().y * 0.02f));
        }
    }
}
