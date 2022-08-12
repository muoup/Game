package com.Game.GUI;

import com.Game.Entity.Enemy;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Inventory.ItemDrag;
import com.Game.GUI.Skills.Skills;
import com.Game.Items.ItemData;
import com.Game.Main.MethodHandler;
import com.Game.World.World;
import com.Game.listener.Input;
import com.Util.Math.DeltaMath;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.util.ArrayList;

public class MouseHover {
    private static int hover = -1;
    private static Enemy hoverEntity;

    public static void init() {

    }

    public static void handleHover(int index) {
        update(index);
        render(index);
    }

    public static void update(int index) {
        // Find Entity Being Hovered If It Exists
        if (!GUI.inGUI()) {
            hover = -1;
            hoverEntity = null;
            ArrayList<Enemy> enemies = MethodHandler.enemies;
            for (int i = 0; i < enemies.size(); i++) {
                Enemy e = enemies.get(i);
                if (e.image == null)
                    return;

                if (!Render.onScreen(e.position, e.image) || !e.enabled)
                    continue;

                if (Vector2.distance(Input.mousePosition, e.position.subtractClone(World.offset)) <= e.image.getHeight()) {
                    hoverEntity = e;
                    return;
                }
            }

            return;
        } else if (GUI.inGUI())
            hoverEntity = null;

        Vector2 deltaMouse = Input.mousePosition.subtractClone(GUI.GuiPos);
        int dx = (int) deltaMouse.x / GUI.intBoxSize;
        int dy = (int) deltaMouse.y / GUI.intBoxSize;

        hover = dx + dy * 4;

        if (hover >= ((index == 0) ? InventoryManager.inventory.length : Skills.skillAmt)) {
            hover = -1;
        }
    }

    public static void render(int index) {
        if (hoverEntity != null) {
            renderHoverText(hoverEntity.name + " | " +
                    DeltaMath.addCommas((int) hoverEntity.health) + " / " +  DeltaMath.addCommas((int) hoverEntity.maxHealth));
        }

        if (hover <= -1 || GUI.renderShop || GUI.renderBank)
            return;

        if (index == 0 && !RightClick.render && !ItemDrag.itemDrag.notEmpty()) { // Handle Item Inventory Hovering
            ItemData item = InventoryManager.getStack(hover);
            String text = "[" + item.getName() + "] - " + ((item.rcOptions.length == 0) ? "Examine" : item.rcOptions[0]);

            if (!item.notEmpty())
                return;

            renderHoverText(text);
        } else if (index == 2) { // Handle Stat Hovering
            String xp = "Current XP: " + DeltaMath.addCommas((int) Skills.getExperience(hover));
            String lvlUp = "XP for Next Level: " + DeltaMath.addCommas(Skills.levelToExp(Skills.getLevel(hover) + 1));
            String until = "XP Left: " + DeltaMath.addCommas((int) (Skills.levelToExp(Skills.getLevel(hover) + 1) - Skills.getExperience(hover)));

            renderHoverText(xp, lvlUp, until);
        }
    }

    private static void renderHoverText(String... text) {
        Render.setFont(new Font("Arial", Font.BOLD, 14));

        float width = 0;

        for (String t : text)
            width = Math.max(Render.getStringWidth(t), width);

        float height = Render.getStringHeight() * text.length;

        float x = (Input.mousePosition.x + width * 1.25f > Settings.screenSize().x) ?
                Input.mousePosition.x - width - 5 : Input.mousePosition.x + 5;
        float y = (Input.mousePosition.y + height * 1.25f > Settings.screenSize().y) ?
                Input.mousePosition.y - height - 5 : Input.mousePosition.y + 5;
        Vector2 dPos = new Vector2(x, y);

        Render.drawRectMultiText(dPos, Color.GRAY, 5, 2, text);
    }
}
