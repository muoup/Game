package com.Game.GUI.Inventory;

import com.Game.GUI.GUI;
import com.Game.GUI.RightClick;
import com.Game.Items.ItemData;
import com.Game.Main.Main;
import com.Util.Math.Vector2;
import com.Util.Other.Render;

import java.awt.*;

public class InventoryManager {

    public static ItemData[] inventory = new ItemData[20];
    public static int draggedIndex = -1;
    public static int useIndex = -1;

    public static void init() {
        for (int i = 0; i < inventory.length; i++) {
            inventory[i] = new ItemData();
        }

        InventoryDrag.init();
    }

    public static void update() {
        if (RightClick.coolDown > 0)
            RightClick.coolDown -= Main.dTime();

        if (RightClick.render)
            useIndex = -1;

        InventoryDrag.update();
    }

    public static void render() {
        Render.setColor(new Color(255, 138, 4));

        Render.drawBounds(GUI.GuiPos, GUI.GUIEnd());

        Render.setColor(Color.BLACK);

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 5; y++) {

                Vector2 rectPos = GUI.getGridPosition(x, y);

                if (draggedIndex == x + y * 4) {
                    Render.drawRectOutline(rectPos, GUI.invSize);
                    continue;
                }

                ItemData stack = getStack(x + y * 4);
                GUI.drawItem(x, y, stack);
            }
        }

        if (useIndex != -1) {
            Render.setColor(Color.GREEN);
            Render.drawRectOutline(GUI.getGridPosition(useIndex % 4, useIndex / 4), GUI.invSize);
        }

        InventoryDrag.render();
    }

    public static ItemData getStack(int index) {
        return inventory[index];
    }

    public static void swapSlots(int index1, int index2) {
        ItemData holder = inventory[index1].clone();
        inventory[index1] = inventory[index2];
        inventory[index2] = holder;
    }

    public static void use(int inventoryIndex) {
        Main.sendPacket("us" + inventoryIndex);
    }

    public static void ClickIdentities(int inventoryIndex) {
        Main.sendPacket("ci" + inventoryIndex);
    }
}