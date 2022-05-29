package com.Game.GUI.Inventory;

import com.Game.Entity.Player;
import com.Game.GUI.GUI;
import com.Game.Items.ItemData;
import com.Game.Main.Main;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;

public class AccessoriesManager {

    public static final int WEAPON_SLOT = 0;
    public static final int AMMO_SLOT = 4;

    public static final int HELMET_SLOT = 3;
    public static final int NECKLACE_SLOT = 7;
    public static final int CHESTPLATE_SLOT = 11;
    public static final int LEGGING_SLOT = 15;
    public static final int BOOT_SLOT = 19;

    public static ItemData[] accessories = new ItemData[23];

    public static void init() {
        for (int i = 0; i < accessories.length; i++) {
            accessories[i] = new ItemData();
        }
    }

    public static ItemData getSlot(int slot) {
        return accessories[slot];
    }

    public static void setSlot(ItemData item, int index) {
        accessories[index] = item;

    }

    public static void render() {
        Render.setColor(GUI.GUIBgColor);
        Render.drawBorderedBounds(GUI.GuiPos, GUI.GUIEnd(), 1);

        for (int i = 0; i < accessories.length; i++) {
            int x = i % 4;
            int y = i / 4;


            switch (i) {
                // Draw highlight images of each under when they are made:
                case WEAPON_SLOT:
                case AMMO_SLOT:
                case HELMET_SLOT:
                case NECKLACE_SLOT:
                case CHESTPLATE_SLOT:
                case LEGGING_SLOT:
                case BOOT_SLOT:
                    GUI.drawMenuItem(x, y, accessories[i]);
                    break;
            }

            Vector2 rectPos = GUI.getGridPosition(x, y);

            if (accessories[i].notEmpty()) {
                Render.drawImage(Render.getScaledImage(accessories[i].getImage(), GUI.invSize), rectPos);
            }
        }
    }

    public static void update() {
        if (GUI.inGUI()) {
            if (Input.GetMouse(1)) {
                Vector2 deltaMouse = Input.mousePosition.subtractClone(GUI.GuiPos);

                int x = (int) deltaMouse.x / GUI.intBoxSize;
                int y = (int) deltaMouse.y / GUI.intBoxSize;

                int index = x + y * 4;

                unEquip(index);
            }
        }
    }

    private static void unEquip(int index) {
        Main.sendPacket("un" + Player.name + ";" + index);
    }

    public static void clientSetItem(int id, String imageID, String examineText, String options) {

    }
}