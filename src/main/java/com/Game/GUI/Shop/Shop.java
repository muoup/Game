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
import com.Util.Other.Sprite;

import java.awt.*;
import java.util.ArrayList;

public class Shop {
    public static final int amountOptions[] = {
            1,
            10,
            100,
            -999,
            Integer.MAX_VALUE,
            -1,
            //-1 // Custom, buy custom or sell all.
    };

    public static ArrayList<ItemData> offeredItems = new ArrayList<>();

    public static ArrayList<Integer> prices = new ArrayList<>();

    public static int selected;
    public static int hover;

    public static String shopVerb = "Buy";
    public static String inventoryVerb = "Sell";

    private static int padding = 16;

    private static final Vector2 beginPos = Settings.curResolution().scale(0.25f);
    private static final Vector2 size = Settings.curResolution().scale(0.5f);
    private static final int maxCol = (int) ((size.x - padding) / (padding + GUI.intBoxSize));

//    public static void setItems(ItemData[] stacks) {
//    }

    public static boolean empty() {
        return offeredItems.size() == 0;
    }

    public static int getLength() {
        return offeredItems.size();
    }

    public static void baseInit() {

    }

    public static void baseRender() {
        if (empty())
            return;

        // Draw basic stuff for every shop such as background and items.
        Render.setColor(Color.LIGHT_GRAY);
        Render.drawBorderedRect(beginPos, size);

        int row = 0;
        int col = 0;

        for (int i = 0; i < getLength(); i++) {
            if (prices.get(i) == -456) {
                row++;
                col = 0;
                continue;
            }

            String text = GUI.formatAmount(prices.get(i));

            Vector2 imageScale = new Vector2(GUI.intBoxSize);

            Vector2 rectPos = beginPos.addClone(padding + (padding + GUI.intBoxSize) * col, padding + (padding + GUI.intBoxSize) * row);
            Vector2 textPos = rectPos.addClone(new Vector2(GUI.intBoxSize - Settings.sWidth(text) - 4, GUI.intBoxSize - 4));

            Render.setColor(Color.BLACK);
            Render.drawRectOutline(rectPos, imageScale);
            Render.drawImage(Render.getScaledImage(offeredItems.get(i).getImage(), imageScale), rectPos);

            col++;

            if (col == maxCol) {
                col = 0;
                row++;
            }

            if (text.contains("-"))
                continue;

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

        int row = 0;
        int col = 0;

        for (int i = 0; i < getLength(); i++) {
            if (prices.get(i) == -456) {
                row++;
                col = 0;
                continue;
            }

            Vector2 imageScale = new Vector2(GUI.intBoxSize);

            Vector2 pos = beginPos.addClone(padding + (padding + GUI.intBoxSize) * col,
                    padding + (padding + GUI.intBoxSize) * row);
            Vector2 pos2 = pos.addClone(imageScale);

            if (Input.mousePosition.greaterThan(pos) && pos2.greaterThan(Input.mousePosition)) {
                if (Input.GetMouseDown(3)) {
                    RightClick.customRightClickFormat(Shop::buyOption,
                            Shop.shopVerb, "%s 1", "%s 10", "%s 100", "%s " + Settings.customAmount, "%s Max", "Examine");
                    selected = i;
                    break;
                } else if (Input.GetMouseDown(1) && !RightClick.render && shopVerb.equals("Buy")) {
                    ChatBox.sendMessage("This item costs " + prices.get(i) + " gold.");
                }
            }

            col++;

            if (col == maxCol) {
                col = 0;
                row++;
            }
        }
    }

    public static void buyOption(int option) {
        if (option >= amountOptions.length) {
            ChatBox.sendMessage(offeredItems.get(selected).examineText);
            if (shopVerb.equals("Buy"))
                ChatBox.sendMessage("This item costs " + prices.get(selected) + " coins each.");
            return;
        }

        Main.sendPacket("si" + Player.name + ";shop;" + selected + ";" + getAmount(option));
    }

    public static void sellOption(int option) {
        if (option >= amountOptions.length) {
            ItemData stack = InventoryManager.getStack(hover);
            ChatBox.sendMessage(stack.examineText);
            if (inventoryVerb.equals("Sell"))
                ChatBox.sendMessage("This item will sell for " + stack.getSellValue() + " coins each");
            return;
        }

        Main.sendPacket("si" + Player.name + ";inventory;" + hover + ";" + getAmount(option));
    }

    public static void addData(String message) {
        String[] data = message.split(";");

        ItemData item = new ItemData();
        item.name = data[0];
        item.image = Sprite.identifierSprite(data[1]);
        item.examineText = data[3];

        offeredItems.add(item);
        prices.add(Integer.parseInt(data[2]));
    }

    public static int getAmount(int index) {
        int amount = amountOptions[index];

        if (amount == -999)
            amount = Settings.customAmount;

        return amount;
    }
}