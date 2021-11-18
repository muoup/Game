package com.Game.GUI.Shop;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.GUI;
import com.Game.GUI.RightClick;
import com.Game.Items.ItemData;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;

public class Shop {
    public static final int amountOptions[] = {
            1,
            10,
            50,
            100,
            -1 // Custom, buy custom or sell all.
    };

    private static ItemData[] offeredItems = new ItemData[0];

    public static int[] prices = null;

    public static int selected;

    private static int padding = 16;

    private static final Vector2 beginPos = Settings.curResolution().scale(0.25f);
    private static final Vector2 size = Settings.curResolution().scale(0.5f);
    private static final int maxRow = (int) ((size.x - padding) / (padding + GUI.intBoxSize));


    // TODO: Item refilling?
    private long timeClosed;

    public static void setItems(ItemData[] stacks) {
        offeredItems = stacks;
    }

    public static boolean empty() {
        return offeredItems.length == 0;
    }

    public static int getLength() {
        return offeredItems.length;
    }

    public static void baseInit() {

    }

    public static void baseRender() {
        if (empty())
            return;

        // Draw basic stuff for every shop such as background and items.
        Render.setColor(Color.LIGHT_GRAY);
        Render.drawBorderedRect(beginPos, size);

        for (int i = 0; i < offeredItems.length; i++) {
            int x = i % maxRow;
            int y = i / maxRow;

            String text = GUI.formatAmount(prices[i]);

            Vector2 imageScale = new Vector2(GUI.intBoxSize);

            Vector2 rectPos = beginPos.addClone(padding + (padding + GUI.intBoxSize) * x, padding + (padding + GUI.intBoxSize) * y);
            Vector2 textPos = rectPos.addClone(new Vector2(GUI.intBoxSize - Settings.sWidth(text) - 4, GUI.intBoxSize - 4));

            Render.setColor(Color.BLACK);
            Render.drawRectOutline(rectPos, imageScale);
            Render.drawImage(Render.getScaledImage(offeredItems[i].getImage(), imageScale), rectPos);

            Render.setColor(new Color(255, 254, 0));
            Render.setFont(Settings.itemFont);
            Render.drawText(text, textPos.addClone(1, 0));
        }

        Render.setColor(Color.RED);
        Render.drawBorderedRect(beginPos.addClone(size.x - GUI.intBoxSize / 2, 0), new Vector2(GUI.intBoxSize / 2));
    }

    public static void baseUpdate() {
        if (Input.GetMouseDown(1)) {
            Vector2 rectBounds = beginPos.addClone(size.x - GUI.intBoxSize / 2, 0);
            if (Input.mouseInBounds(rectBounds, rectBounds.addClone(GUI.intBoxSize / 2))) {
                GUI.disableShop();
                return;
            }
        }

        for (int i = 0; i < offeredItems.length; i++) {
            int x = i % maxRow;
            int y = i / maxRow;

            Vector2 imageScale = new Vector2(GUI.intBoxSize);

            Vector2 pos = beginPos.addClone(padding + (padding + GUI.intBoxSize) * x, padding + (padding + GUI.intBoxSize) * y);
            Vector2 pos2 = pos.addClone(imageScale);

            if (Input.mousePosition.greaterThan(pos) && pos2.greaterThan(Input.mousePosition)) {
                if (Input.GetMouseDown(3)) {
                    RightClick.customRightClick((int option) -> buyOption(option),
                            "Buy 1", "Buy 10", "Buy 50", "Buy 100");

                    selected = i;
                    break;
                } else if (Input.GetMouseDown(1)) {
                    ChatBox.sendMessage("This item costs " + prices[i] + " gold.");
                }
            }
        }
    }

    public static void buyOption(int option) {
//        int multiplier = amountOptions[option];
//        int indPrice = selected.getPrice();
//        int price = indPrice * multiplier;
//
//        int gold = InventoryManager.getAmount(ItemList.gold);
//
//        if (gold < price) {
//            multiplier = gold / indPrice;
//            price = multiplier * indPrice;
//        }
//        if (multiplier == 0) {
//            ChatBox.sendMessage("You are out of money!");
//            return;
//        }
//
//        multiplier = Math.min(multiplier, InventoryManager.emptySpace() * selected.getMaxAmount());
//
//        InventoryManager.removeItem(ItemList.gold, price);
//        InventoryManager.addItem(selected.getItemList(), multiplier);
    }

    public static void sellOption(int option) {
//        int multiplier = amountOptions[option];
//        int amt = InventoryManager.getAmount(selected.getItemList(), selected.getData());
//
//        if (multiplier == -1 || amt < multiplier) {
//            multiplier = amt;
//        }
//
//        if (multiplier == 0) {
//            ChatBox.sendMessage("You do not have any of this item!");
//            return;
//        }
//
//        InventoryManager.removeItem(selected.getItemList(), multiplier, selected.getData());
//        InventoryManager.addItem(ItemList.gold, (int) (selected.getPrice() * multiplier * 0.95f));
    }
}