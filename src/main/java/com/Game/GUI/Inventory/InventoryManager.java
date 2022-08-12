package com.Game.GUI.Inventory;

import com.Game.Entity.Player;
import com.Game.GUI.GUI;
import com.Game.Items.ItemData;
import com.Game.Main.Main;
import com.Util.Math.Vector2;
import com.Util.Other.Render;

import java.awt.*;

public class InventoryManager {

    public static class InvTimer {
        long duration;
        long finishTime;

        public InvTimer(long duration, long finishTime) {
            this.duration = duration;
            this.finishTime = finishTime;
        }

        public long getDuration() {
            return duration;
        }

        public long getFinishTime() {
            return finishTime;
        }
    }

    public static ItemData[] inventory = new ItemData[20];
    private static InvTimer[] invTimers = new InvTimer[20];
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
                int index = x + y * 4;

                Vector2 rectPos = GUI.getGridPosition(x, y);

                if (ItemDrag.invDragIndex() == index) {
                    Render.setColor(Color.BLACK);
                    Render.drawRectOutline(rectPos, GUI.invSize);
                    continue;
                }

                ItemData stack = getStack(index);
                GUI.drawMenuItem(x, y, stack);

                if (invTimers[index] != null) {
                    if (invTimers[index].getFinishTime() <= System.currentTimeMillis()) {
                        invTimers[index] = null;
                        return;
                    }

                    GUI.drawInvTimer(x, y, invTimers[index]);
                }
            }
        }
    }

    public static ItemData getStack(int index) {
        return inventory[index];
    }

    public static void swapSlots(int index1, int index2) {
        Main.sendPacket("ss" + Player.name + ";" + index1 + ";" + index2);

        InvTimer temp = invTimers[index1];
        invTimers[index1] = invTimers[index2];
        invTimers[index2] = temp;
    }

    public static void leftClick(int inventoryIndex) {
        Main.sendPacket("lc" + Player.name + ";" + inventoryIndex);
    }

    public static void handleTimerAdd(String message) {
        String[] parts = message.split(";");
        int index = Integer.parseInt(parts[0]);
        long duration = Long.parseLong(parts[1]);
        long finishTime = Long.parseLong(parts[2]);

        invTimers[index] = new InvTimer(duration, finishTime);
    }
}