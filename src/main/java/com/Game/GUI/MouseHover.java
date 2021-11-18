package com.Game.GUI;

import com.Game.Entity.Enemy;
import com.Game.GUI.Inventory.InventoryDrag;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Skills.Skills;
import com.Game.Items.ItemData;
import com.Game.Main.MethodHandler;
import com.Game.World.World;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;

public class MouseHover {
    private static int hover = -1;
    private static Vector2 draw = Vector2.zero();
    private static final int offset = 16;
    private static Enemy hoverEntity;

    public static void init() {

    }

    public static void handleHover(int index) {
        update(index);
        render(index);
        updateEntity();
        renderEntity();
    }

    public static void update(int index) {
        if (!GUI.inGUI()) {
            hover = -1;
            return;
        }
        Vector2 deltaMouse = Input.mousePosition.subtractClone(GUI.GuiPos);
        int dx = (int) deltaMouse.x / GUI.intBoxSize;
        int dy = (int) deltaMouse.y / GUI.intBoxSize;

        hover = dx + dy * 4;
        draw = deltaMouse.clone();

        if (hover >= ((index == 0) ? InventoryManager.inventory.length : Skills.skillAmt)) {
            hover = -1;
            draw = Vector2.zero();
        }
    }

    public static void render(int index) {
        if (hover <= -1 || GUI.renderShop)
            return;

        if (index == 0 && !RightClick.render && InventoryDrag.itemDrag.notEmpty()) { // Handle Item Inventory Hovering
            ItemData item = InventoryManager.getStack(hover);
            String text = item.getName();

            if (!item.notEmpty())
                return;

            if (InventoryManager.useIndex != -1) {
                ItemData use = InventoryManager.getStack(InventoryManager.useIndex);
                if (use.notEmpty())
                    text = "Use " + use.getName() + "" +
                            " on " + item.getName();
            }
            Render.setFont(new Font("Arial", Font.BOLD, 14));

            float width = Settings.sWidth(text) * RightClick.maxMultiplier;
            float x = (Input.mousePosition.x + width * 1.25f > Settings.curResolution().x) ?
                    Input.mousePosition.x - width * 1.1f : Input.mousePosition.x + offset;
            Vector2 dPos = new Vector2(x + width * (1 - RightClick.maxMultiplier) / 2, Input.mousePosition.y);

            Render.setColor(Color.BLACK);
            Render.drawRectangle(dPos, new Vector2(width * 1.15f, Render.getStringHeight() * 1.55f));

            Render.setColor(Color.GRAY);
            Render.drawRectangle(dPos.addClone(2, 2),
                    new Vector2(width * 1.15f - 4, Render.getStringHeight() * 1.55f - 4));

            Render.setColor(Color.BLACK);
            Render.drawText(text, dPos.addClone(width * 0.05f, Settings.curResolution().y * 0.02f));
        } else if (index == 2) { // Handle Stat Hovering
            String xp = "Current XP: " + (int) Skills.getExperience(hover);
            String lvlUp = "XP for Next Level: " + Skills.levelToExp(Skills.getLevel(hover) + 1);

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
            Render.drawText(Skills.skillNames[hover], dPos.addClone(width * 0.05f, Settings.curResolution().y * 0.02f));
            Render.drawText(xp, dPos.addClone(width * 0.05f, Settings.curResolution().y * 0.04f));
            Render.drawText(lvlUp, dPos.addClone(width * 0.05f, Settings.curResolution().y * 0.06f));
        }
    }

    public static void updateEntity() {
        if (!GUI.inGUI()) {
            hover = -1;
            draw = Vector2.zero();
            hoverEntity = null;
            for (Enemy e : MethodHandler.enemies) {
                if (e.image == null)
                    return;

                if (!Render.onScreen(e.position, e.image) || !e.enabled)
                    continue;
                if (Vector2.distance(Input.mousePosition, e.position.subtractClone(World.offset)) <= e.image.getHeight()) {
                    draw = Input.mousePosition.clone();
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
