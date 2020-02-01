package com.Game.GUI.Shop;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.GUI;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.RightClick;
import com.Game.Items.ItemList;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;

public class Shop {
    public static Shop empty = new Shop(new ItemList[0]);
    public static Shop weapons = new Shop(new ItemList[] {
            ItemList.arrow,
            ItemList.bow,
            ItemList.clownfish
    });

    public static ItemList selected = null;

    private static final int amountOptions[] = {
            1,
            10,
            50,
            100,
            -1 // Custom, buy custom or sell all.
    };

    private static int padding = 16;

    private static final Vector2 beginPos = Settings.curResolution().scale(0.25f);
    private static final Vector2 size = Settings.curResolution().scale(0.5f);
    private static final int maxRow = (int) ((size.x - padding) / (padding + GUI.IntBoxSize));

    private ItemList[] offeredItems;

    // TODO: Item refilling?
    private long timeClosed;

    public Shop(ItemList[] stacks) {
        this.offeredItems = stacks;
    }

    public boolean empty() {
        return offeredItems.length == 0;
    }

    public void baseInit() {

    }

    public void baseRender() {
        if (empty())
            return;

        // Draw basic stuff for every shop such as background and items.
        Render.setColor(Color.LIGHT_GRAY);
        Render.drawBorderedRect(beginPos, size);

        for (int i = 0; i < offeredItems.length; i++) {
            int x = i % maxRow;
            int y = i / maxRow;

            String text = GUI.formatAmount(offeredItems[i].getPrice());

            Vector2 imageScale = new Vector2(GUI.IntBoxSize);

            Vector2 rectPos = beginPos.addClone(padding + (padding + GUI.IntBoxSize) * x, padding + (padding + GUI.IntBoxSize) * y);
            Vector2 textPos = rectPos.addClone(new Vector2(GUI.IntBoxSize - Settings.sWidth(text) - 4, GUI.IntBoxSize - 4));

            Render.setColor(Color.BLACK);
            Render.drawRectOutline(rectPos, imageScale);
            Render.drawImage(Render.getScaledImage(offeredItems[i].getImage(), imageScale), rectPos);

            Render.setColor(new Color(255, 254, 0));
            Render.setFont(Settings.itemFont);
            Render.drawText(text, textPos.addClone(1, 0));
        }
    }

    public void baseUpdate() {
        if (!Input.GetMouseDown(3))
            return;

        for (int i = 0; i < offeredItems.length; i++) {
            int x = i % maxRow;
            int y = i / maxRow;

            Vector2 pos = beginPos.addClone(padding + (padding + 48) * x, padding + (padding + 48) * y);
            Vector2 pos2 = pos.addClone(Render.getImageSize(offeredItems[i].getImage()));

            if (Input.mousePosition.greaterThan(pos) && pos2.greaterThan(Input.mousePosition)) {
                RightClick.customRightClick((int option) -> buyOption(option),
                        "Buy 1", "Buy 10", "Buy 50", "Buy 100");

                selected = offeredItems[i];
                break;
            }
        }
    }

    public void buyOption(int option) {
        int multiplier = amountOptions[option];

        int price = selected.getPrice() * multiplier;

        int gold = InventoryManager.getAmount(ItemList.gold);

        if (gold < price) {
            multiplier = gold / selected.getPrice();
            price = multiplier * selected.getPrice();
        }

        if (multiplier == 0) {
            ChatBox.sendMessage("You are out of money!");
        }

        InventoryManager.removeItem(ItemList.gold, price);
        InventoryManager.addItem(selected, multiplier);
    }

    public void sellOption(int option) {
        int multiplier = amountOptions[option];
        int amt = InventoryManager.getAmount(selected);

        if (amt < multiplier) {
            multiplier = amt;
        }

        if (multiplier == 0) {
            ChatBox.sendMessage("You do not have any of this item!");
            return;
        }

        InventoryManager.removeItem(selected, multiplier);
        InventoryManager.addItem(ItemList.gold, (int) (selected.getPrice() * multiplier * 0.95f));
    }
}