package com.Game.GUI.Banking;

import com.Game.Entity.Player;
import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.GUI;
import com.Game.GUI.GUIWindow.BasicGUIWindow;
import com.Game.GUI.RightClick;
import com.Game.GUI.Shop.Shop;
import com.Game.Items.ItemData;
import com.Game.Main.Main;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.util.ArrayList;

public class BankingHandler extends BasicGUIWindow {
    public static ArrayList<ItemData> items = new ArrayList();

    public static int padding = 16;

    private static final Vector2 beginPos = Settings.curResolution().scale(0.25f);
    private static final Vector2 size = Settings.curResolution().scale(0.5f);
    public static final int maxRow = (int) ((size.x - padding) / (padding + GUI.intBoxSize));

    public static int hover = -1;

    public static float draggedIndex = -1;

    public static void render() {
        // Draw basic stuff for every shop such as background and items.
        Render.setColor(new Color(182, 124, 45));
        Render.drawBorderedRect(beginPos, size);

        for (int i = 0; i < items.size(); i++) {
            int x = i % maxRow;
            int y = i / maxRow;

            String text = GUI.formatAmount(items.get(i).getAmount());

            Vector2 imageScale = new Vector2(GUI.intBoxSize);

            Vector2 rectPos = beginPos.addClone(padding + (padding + GUI.intBoxSize) * x, padding + (padding + GUI.intBoxSize) * y);
            Vector2 textPos = rectPos.addClone(new Vector2(GUI.intBoxSize - Settings.sWidth(text) - 4, GUI.intBoxSize - 4));

            Render.setColor(Color.BLACK);
            Render.drawRectOutline(rectPos, imageScale);

            if (draggedIndex == i)
                continue;

            Render.drawImage(Render.getScaledImage(items.get(i).getImage(), imageScale), rectPos);

            Render.setColor(Color.BLACK);
            Render.setFont(Settings.itemFont);
            Render.drawText(text, textPos.addClone(1, 0));
        }

        Render.setColor(Color.RED);
        Render.drawBorderedRect(beginPos.addClone(size.x - GUI.intBoxSize / 2, 0), new Vector2(GUI.intBoxSize / 2));
    }

    public static void update() {
        if (Input.GetMouseDown(1)) {
            Vector2 rectBounds = beginPos.addClone(size.x - GUI.intBoxSize / 2, 0);
            if (Input.mouseInBounds(rectBounds, rectBounds.addClone(GUI.intBoxSize / 2))) {
                GUI.closeBank();
                return;
            }
        }

        if (!Input.GetMouseDown(3))
            return;

        for (int i = 0; i < items.size(); i++) {
            int x = i % maxRow;
            int y = i / maxRow;

            Vector2 imageScale = new Vector2(GUI.intBoxSize);

            Vector2 pos = beginPos.addClone(padding + (padding + GUI.intBoxSize) * x, padding + (padding + GUI.intBoxSize) * y);
            Vector2 pos2 = pos.addClone(imageScale);

            if (Input.mousePosition.greaterThan(pos) && pos2.greaterThan(Input.mousePosition)) {
                hover = i;
                RightClick.customRightClick(BankingHandler::withdrawItem,
                        "Withdraw 1", "Withdraw 10", "Withdraw 50", "Withdraw 100", "Withdraw All", "Examine");

                break;
            }
        }
    }

    private static void withdrawItem(int option) {
        if (option >= Shop.amountOptions.length) {
            ItemData item = items.get(hover);

            ChatBox.sendMessage(item.examineText);

            int stackCount = item.amount;

            if (!item.examineText.contains(Integer.toString(stackCount)) && stackCount > 1)
                ChatBox.sendMessage("This stack contains " + stackCount + " items.");
            return;
        }

        Main.sendPacket("bc" + Player.name + ";withdraw;" + hover + ";" + Shop.amountOptions[option]);
    }

    public static void depositItem(int option) {
        if (option >= Shop.amountOptions.length) {
            ItemData item = items.get(hover);

            ChatBox.sendMessage(item.examineText);

            int stackCount = item.amount;

            if (!item.examineText.contains(Integer.toString(stackCount)) && stackCount > 1)
                ChatBox.sendMessage("This stack contains " + stackCount + " items.");

            return;
        }

        Main.sendPacket("bc" + Player.name + ";deposit;" + hover + ";" + Shop.amountOptions[option]);
    }

    public static void handleChange(String message) {
        String[] parts = message.split(";");
        int index;

        switch (parts[0]) {
            case "add":
                items.add(ItemData.getFromPacketData(message));
                break;
            case "change":
                index = Integer.parseInt(parts[1]);
                int amount = Integer.parseInt(parts[2]);
                items.get(index).addAmount(amount);
                break;
            case "remove":
                index = Integer.parseInt(parts[1]);
                items.remove(index);
                break;
            case "replace":
                String[] portions = message.split("=>");
                index = Integer.parseInt(portions[0].replace("replace;", ""));
                ItemData stack = ItemData.getFromPacketData("0;" + portions[1]);
                items.set(index, stack);
                break;
        }
    }

    public static ItemData getItem(int bankIndex) {
        return items.get(bankIndex);
    }

    public static void swapSlots(int bankIndex, int index) {
        Main.sendPacket("bs" + Player.name + ";" + bankIndex + ";" + index);
    }

    public static int hoverIndex() {
        if (!GUI.inBank())
            return -1;

        for (int i = 0; i < items.size(); i++) {
            int x = i % maxRow;
            int y = i / maxRow;

            Vector2 imageScale = new Vector2(GUI.intBoxSize);

            Vector2 pos = beginPos.addClone(padding + (padding + GUI.intBoxSize) * x, padding + (padding + GUI.intBoxSize) * y);
            Vector2 pos2 = pos.addClone(imageScale);

            if (Input.mousePosition.greaterThan(pos) && pos2.greaterThan(Input.mousePosition)) {
                return i;
            }
        }

        return -1;
    }

    public static void leftClick(int index) {
        Main.sendPacket("wb" + Player.name + ";" + index);
    }
}
