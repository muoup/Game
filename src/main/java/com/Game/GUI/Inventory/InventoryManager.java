package com.Game.GUI.Inventory;

import com.Game.Entity.Player;
import com.Game.GUI.GUI;
import com.Game.Items.ItemData;
import com.Game.Main.Main;
import com.Util.Math.Vector2;
import com.Util.Other.Render;

import java.awt.*;

public class InventoryManager {

    public static ItemData[] inventory = new ItemData[20];
    public static int draggedIndex = -1;

    public static void init() {
        for (int i = 0; i < inventory.length; i++) {
            inventory[i] = new ItemData();
        }

        ItemDrag.init();
    }

    public static void update() {
    }

    public static void render() {
        Render.setColor(new Color(180, 120, 68, 152));

        Render.drawBounds(GUI.GuiPos, GUI.GUIEnd());

        Render.setColor(Color.BLACK);

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 5; y++) {

                Vector2 rectPos = GUI.getGridPosition(x, y);

                if (ItemDrag.invDragIndex() == x + y * 4) {
                    Render.setColor(Color.BLACK);
                    Render.drawRectOutline(rectPos, GUI.invSize);
                    continue;
                }

                ItemData stack = getStack(x + y * 4);
                GUI.drawMenuItem(x, y, stack);
            }
        }
    }

    public static ItemData getStack(int index) {
        return inventory[index];
    }

    public static void swapSlots(int index1, int index2) {
        Main.sendPacket("ss" + Player.name + ";" + index1 + ";" + index2);
    }

    public static void leftClick(int inventoryIndex) {
        Main.sendPacket("lc" + Player.name + ";" + inventoryIndex);
    }
}