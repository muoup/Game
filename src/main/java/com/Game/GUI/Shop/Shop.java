package com.Game.GUI.Shop;

import com.Game.Entity.Player;
import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.GUI;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.RightClick;
import com.Game.Items.ItemData;
import com.Game.Main.Main;
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
            -1,
            //-1 // Custom, buy custom or sell all.
    };

    public static ItemData[] offeredItems = new ItemData[0];

    public static int[] prices = null;

    public static int selected;
    public static int hover;

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

            Render.setColor(new Color(249, 249, 34));
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
                    RightClick.customRightClick(Shop::buyOption,
                            "Buy 1", "Buy 10", "Buy 50", "Buy 100", "Buy " + Settings.customAmount, "Examine");

                    selected = i;
                    break;
                } else if (Input.GetMouseDown(1) && !RightClick.render) {
                    ChatBox.sendMessage("This item costs " + prices[i] + " gold.");
                }
            }
        }
    }

    public static void buyOption(int option) {
        if (option >= amountOptions.length) {
            ChatBox.sendMessage(offeredItems[selected].examineText);
            ChatBox.sendMessage("This item costs " + prices[selected] + " coins each.");
            return;
        }

        int amount = amountOptions[option];

        Main.sendPacket("si" + Player.name + ";buy;" + selected + ";" + amountOptions[option]);
    }

    public static void sellOption(int option) {
        if (option >= amountOptions.length) {
            ItemData stack = InventoryManager.getStack(hover);
            ChatBox.sendMessage(stack.examineText);
            ChatBox.sendMessage("This item will sell for " + stack.getSellValue() + " coins each");
            return;
        }

        Main.sendPacket("si" + Player.name + ";sell;" + hover + ";" + amountOptions[option]);
    }
}