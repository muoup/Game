package com.Game.GUI;

import com.Game.Entity.Player;
import com.Game.GUI.Banking.BankingHandler;
import com.Game.GUI.Inventory.ItemDrag;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Shop.Shop;
import com.Game.Items.ItemData;
import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Game.Object.GameObject;
import com.Game.Object.GroundItem;
import com.Game.Object.UsableGameObject;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RightClick {
    public static Vector2 draw = Vector2.zero();
    public static Vector2 deltaDraw = Vector2.zero();
    public static boolean render = false;
    public static State state = State.none;
    public static int groundItem;
    public static UsableGameObject object = null;
    public static ArrayList<String> options;
    public static float percentBox;
    private static float maxWidth = 0;
    public static float maxMultiplier = 1.1f;
    public static float coolDown = 0;
    public static RightClickRunnable run = null;

    public static void init() {
        options = new ArrayList<>();
        percentBox = Settings.curResolution().y * 0.03f;
    }

    public static void render() {
        if (!render)
            return;

        if (options.size() == 0) {
            reset();
            return;
        }

        Render.setFont(Settings.groundFont);

        for (int y = 0; y < options.size(); y++) {
            Vector2 newPos = deltaDraw.addClone(0, percentBox * y);

            Render.setColor(Color.LIGHT_GRAY);
            Render.drawRectangle(newPos, new Vector2(maxWidth, percentBox));

            Render.setColor(Color.BLACK);
            Render.drawRectOutline(newPos, new Vector2(maxWidth, percentBox));
            Render.drawText(options.get(y), newPos.addClone(maxWidth * (maxMultiplier - 1) / 2, percentBox * 0.65f));
        }
    }

    public static void update() {
        if (!render)
            state = State.none;

        if (Input.GetMouseDown(3) && state == State.none) {
            groundItem = 0;
            state = (GUI.inGUI()) ? State.inventory : State.none;

            if (state == State.inventory && GUI.curMain == 0)
                inventoryRightClick();

            if (state == State.none)
                groundRightClick();

            coolDown = 0.1f;
        }

        if (render && !onPopup()) {
            reset();
            return;
        }

        if (render && Input.GetMouse(1)) {
            // Select Right Click Option

            if (state == State.inventory) {
                Vector2 deltaDraw = draw.subtractClone(GUI.GuiPos);

                int xx = (int) deltaDraw.x / GUI.intBoxSize;
                int yy = (int) deltaDraw.y / GUI.intBoxSize;

                ItemData item = InventoryManager.inventory[xx + yy * 4];

                int option = (int) Input.mousePosition.subtractClone(draw).y / (int) percentBox;

                item.onRightClick(xx + yy * 4, options.get(option));

                reset();
            } else if (state == State.groundItem) {
                if (MethodHandler.groundItems.size() - 1 < groundItem) {
                    reset();
                    return;
                }

                if (Vector2.distance(Player.position, MethodHandler.groundItems.get(groundItem).position) > 512) {
                    reset();
                    return;
                }

                int option = (int) Input.mousePosition.subtract(draw).y / (int) percentBox;
                int token = MethodHandler.groundItems.get(groundItem).randomToken;

                Main.sendPacket("gi" + Player.name + ";" + token + ";" + option);

                reset();
            } else if (state == State.object) {
                if (Vector2.distance(Player.position, object.position) > 512) {
                    reset();
                    return;
                }

                int option = (int) Input.mousePosition.subtractClone(deltaDraw).y / (int) percentBox;

                if (option > options.size() - 1) {
                    reset();
                    return;
                }

                object.onOption(option);

                reset();
            } else if (state == State.misc) {
                int option = (int) Input.mousePosition.subtractClone(deltaDraw).y / (int) percentBox;

                if (option > options.size() - 1) {
                    reset();
                    return;
                }

                if (run != null)
                    run.run(option);

                reset();
            }
        }
    }

    private static void reset() {
        render = false;
        run = null;
        state = State.none;
    }

    public static void customRightClick(ArrayList<String> optionArray, RightClickRunnable run) {
        render = true;
        state = State.misc;
        draw = Input.mousePosition.clone();
        deltaDraw = draw.clone();
        maxWidth = 0;
        RightClick.run = run;

        options.clear();

        Render.setFont(Settings.groundFont);

        options.addAll(optionArray);

        for (String s : options) {
            if (Render.getStringWidth(s) > maxWidth)
                maxWidth = Render.getStringWidth(s);
        }

        maxWidth *= maxMultiplier;

        if (deltaDraw.x + maxWidth > Settings.curResolution().x * 0.9f)
            deltaDraw.x -= maxWidth;

        if (deltaDraw.y + options.size() * percentBox > Settings.curResolution().y * 0.9) {
            deltaDraw.y -= options.size() * percentBox;
        }
    }

    public static void customRightClick(RightClickRunnable run, String... options) {
        ArrayList<String> optionArray = new ArrayList<>(Arrays.asList(options));

        customRightClick(optionArray, run);
    }

    public static void customRightClickFormat(RightClickRunnable run, String format, String... options) {
        ArrayList<String> optionArray = new ArrayList<>();

        for (String s : options) {
            optionArray.add(String.format(s, format));
        }

        customRightClick(optionArray, run);
    }

    private static void inventoryRightClick() {
        if (GUI.renderShop && !Shop.inventoryVerb.trim().equals("N/A")) {
            if (inventoryStack().notEmpty()) {
                Shop.hover = getHoverIndex();
                customRightClickFormat(Shop::sellOption, Shop.inventoryVerb, "%s 1", "%s 10",
                        "%s 50", "%s 100", "%s All", "Examine");
            }
            return;
        }  else if (GUI.renderBank) {
            if (inventoryStack().notEmpty()) {
                BankingHandler.hover = getHoverIndex();
                customRightClick(BankingHandler::depositItem, "Deposit 1", "Deposit 10",
                        "Deposit 50", "Deposit 100", "Deposit All", "Examine");
            }
            return;
        }

        if (ItemDrag.itemDrag.notEmpty())
            return;

        maxWidth = 0;

        state = State.inventory;

        ItemData item = inventoryStack();

        if (!item.notEmpty() || item.getAmount() == 0)
            return;

        render = true;
        draw = Input.mousePosition.clone();
        deltaDraw = draw.clone();

        options.clear();

        Render.setFont(Settings.groundFont);

        Collections.addAll(options, item.getOptions());

        for (String s : options) {
            if (Render.getStringWidth(s) > maxWidth)
                maxWidth = Render.getStringWidth(s);
        }

        maxWidth = Math.max(Settings.sWidth("Examine"), maxWidth) * maxMultiplier;

        if (deltaDraw.x + maxWidth > Settings.curResolution().x * 0.9f)
            deltaDraw.x -= maxWidth;

        options.add("Drop");
        options.add("Examine");
    }

    public static ItemData inventoryStack() {
        Vector2 deltaMouse = Input.mousePosition.subtractClone(GUI.GuiPos);

        int xx = (int) deltaMouse.x / GUI.intBoxSize;
        int yy = (int) deltaMouse.y / GUI.intBoxSize;

        return InventoryManager.inventory[xx + yy * 4];
    }

    public static int getHoverIndex() {
        Vector2 deltaMouse = Input.mousePosition.subtractClone(GUI.GuiPos);

        int xx = (int) deltaMouse.x / GUI.intBoxSize;
        int yy = (int) deltaMouse.y / GUI.intBoxSize;

        return xx + yy * 4;
    }

    private static void groundRightClick() {
        GroundItem hover = GroundItem.mouseOver();
        UsableGameObject object = GameObject.mouseOver();

        if (hover == null && object != null) {
            objectRightClick(object);
            return;
        } else if (hover == null)
            return;

        state = State.groundItem;

        groundItem = MethodHandler.groundItems.indexOf(hover);

        maxWidth = 0;

        render = true;
        draw = Input.mousePosition.clone();
        deltaDraw = draw.clone();

        options.clear();

        Render.setFont(Settings.groundFont);

        for (ItemData item : hover.itemStacks) {
            String content = item.getAmount() + " " + item.name;
            if (item.getAmount() > 1)
                content += "s";

            if (Settings.sWidth(content) > maxWidth)
                maxWidth = Settings.sWidth(content);

            options.add(content);
        }

        maxWidth *= maxMultiplier;

        if (maxWidth + deltaDraw.x > Settings.curResolution().x)
            deltaDraw.x = Settings.curResolution().x - maxWidth;
    }

    private static void objectRightClick(UsableGameObject object) {
        //onRightClick.object.loseFocus();
        RightClick.object = object;

        if (object == null)
            return;

        state = State.object;

        maxWidth = 0;

        render = true;
        draw = Input.mousePosition.clone();
        deltaDraw = draw.clone();

        maxWidth = 0;

        object.onRightClick();

        options.clear();
        options.addAll(object.options);

        for (String s : options) {
            maxWidth = Math.max(maxWidth, Render.getStringWidth(s));
        }

        maxWidth *= maxMultiplier;

        Render.setFont(Settings.groundFont);

        if (maxWidth + deltaDraw.x > Settings.curResolution().x)
            deltaDraw.x = Settings.curResolution().x - maxWidth;
    }

    public static boolean onPopup() {
        Vector2 top = deltaDraw.subtractClone(15f, 15f);
        Vector2 bottom = deltaDraw.addClone(maxWidth + 15f, percentBox * options.size() + 15f);

        return Input.mousePosition.compareTo(top) == 1 && Input.mousePosition.compareTo(bottom) == -1;
    }
}

enum State {
    none, inventory, groundItem, object, misc
}