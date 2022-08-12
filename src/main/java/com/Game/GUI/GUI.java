package com.Game.GUI;

import com.Game.Entity.Player;
import com.Game.GUI.Banking.BankingHandler;
import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Inventory.ItemDrag;
import com.Game.GUI.Shop.Shop;
import com.Game.GUI.Skills.Skills;
import com.Game.GUI.Skills.SkillsManager;
import com.Game.Items.ItemData;
import com.Game.Main.Main;
import com.Game.Questing.QuestManager;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.util.ArrayList;

public class GUI {

    private static float coolDown = 0f;
    private static Image[] inventoryOptions;
    public static int select, intBoxSize, curMain = 0;
    public static Vector2 below, GuiPos, invSize, categorySize;

    private static final String[] invImgNames = {
            "backpack.png",
            "accessories.png",
            "levels.png",
            "questing.png"
    };

    public static boolean renderBank = false;
    public static boolean renderShop = false;

    public static ArrayList<String> extraCommands = new ArrayList<>();

    public static final Color GUIBgColor = new Color(128, 128, 128, 120);

    public static Vector2 GUIEnd() {
        return GuiPos.addClone(4 * intBoxSize, 5 * intBoxSize);
    }

    public static boolean inGUI() {
        return Input.mouseInBounds(GuiPos, GUIEnd());
    }

    public static boolean inBank() {
        return Input.mouseInBounds(Settings.screenSize().scaleClone(0.25f), Settings.screenSize().scaleClone(0.75f)) && renderBank;
    }

    public static Vector2 getGridPosition(int x, int y) {
        return new Vector2(x, y).scale(GUI.intBoxSize).add(GuiPos);
    }

    public static void init() {
        Vector2 res = Settings.screenSize();
        inventoryOptions = new Image[invImgNames.length];
        intBoxSize = (int) (res.x * 0.05f);
        invSize = Vector2.identity(intBoxSize);
        select = (int) (intBoxSize * 0.75);
        categorySize = Vector2.identity(select);
        Vector2 mainOffset = new Vector2(intBoxSize * 0.5f, intBoxSize * 1.5f);
        GuiPos = res.subtractClone(new Vector2(intBoxSize * 4f, intBoxSize * 5.5f)).subtractClone(mainOffset);
        below = GuiPos.addClone(0, intBoxSize * 5);

        for (int i = 0; i < invImgNames.length; i++) {
            inventoryOptions[i] = Render.getScaledImage(Main.getImage("GUI/" + invImgNames[i]), select, select);
        }

        InventoryManager.init();
        AccessoriesManager.init();
        SkillsManager.init();
        Skills.initExperience();
        RightClick.init();
        ChatBox.init();
    }

    public static void render() {
        Vector2 offset = below.clone();
        Vector2 change = new Vector2(select, 0);

        for (int i = 0; i < inventoryOptions.length; i++) {
            Render.setColor((curMain == i) ? Color.GRAY : Color.LIGHT_GRAY);

            Render.drawRectangle(offset, categorySize);

            Render.drawImage(inventoryOptions[i], offset);

            Render.setColor(Color.BLACK);

            Render.drawRectOutline(offset, categorySize);

            offset.add(change);
        }

        if (coolDown > 0) {
            coolDown -= Main.dTime();
        }

        if (RightClick.coolDown > 0 && !RightClick.render)
            RightClick.coolDown -= Main.dTime();

        handleInventory();

        ChatBox.update();
        ChatBox.render();

        if (renderShop) {
            Shop.baseRender();
            Shop.baseUpdate();
            Shop.handleCommands();
        }

        else if (renderBank) {
            BankingHandler.update();
            BankingHandler.render();
        }

        ItemDrag.render();
        ItemDrag.update();

        RightClick.update();
        RightClick.render();

        MouseHover.handleHover(curMain);
    }

    public static void update() {
        Vector2 end = below.addClone(new Vector2(intBoxSize * inventoryOptions.length, intBoxSize));

        if (Input.mousePosition.compareTo(below) == 1 && end.compareTo(Input.mousePosition) == 1 && Input.GetMouse(1) && !RightClick.render && RightClick.coolDown <= 0 && !ItemDrag.itemDrag.notEmpty()) {
            Vector2 mouseOffset = Input.mousePosition.subtractClone(below);

            int selection = (int) mouseOffset.x / (int) categorySize.x;

            if (selection >= inventoryOptions.length)
                return;

            curMain = selection;

            coolDown = 0.2f;
        }
    }

    public static void handleInventory() {
        switch (curMain) {
            case 0:
                InventoryManager.render();
                InventoryManager.update();
                break;
            case 1:
                AccessoriesManager.render();
                AccessoriesManager.update();
                break;
            case 2:
                SkillsManager.render();
                SkillsManager.update();
                break;
            case 3:
                QuestManager.render();
                QuestManager.update();
                break;
            default:
                System.err.println("There is something wrong.");
                System.err.println(curMain);
                break;
        }
    }

    public static String formatAmount(int amount) {
        if (amount >= 1000000000) {
            return amount / 1000000000 + "b";
        } else if (amount >= 1000000) {
            return amount / 1000000 + "m";
        } else if (amount >= 1000) {
            return amount / 1000 + "k";
        } else {
            return amount + "";
        }
    }

    public static void drawMenuItem(int x, int y, ItemData stack) {
        Vector2 rectPos = getGridPosition(x, y);

        drawOutlinedItem(rectPos, stack);
    }

    public static void drawOutlinedItem(Vector2 rectPos, ItemData item) {
        Render.setColor(Color.BLACK);
        Render.drawRectOutline(rectPos, GUI.invSize);

        drawItem(rectPos, item);
    }

    public static void drawItem(Vector2 rectPos, ItemData item) {
        Render.setColor(Color.BLACK);
        Render.setFont(Settings.itemFont);

        if (item.getSprite() == null)
            return;

        Render.drawImage(Render.getScaledImage(item.getImage(), GUI.invSize), rectPos);

        // If the stack of items contains more than one item, make sure to write the amount of items at the bottom
        // right corner of the item box.
        if (item.getAmount() > 1) {
            String text = formatAmount(item.getAmount());

            Vector2 textPos = rectPos.addClone(new Vector2(GUI.intBoxSize - Settings.sWidth(text) - 4, GUI.intBoxSize - 4));

            // Draws the amount of items in the item stack along with a white rectangle on top of it so that
            // the text is easier to read.
//
//            Render.setColor(new Color(255, 255, 255, 150));
//            Render.drawRectangle(textPos.subtractClone(Render.getStringWidth(text) * 0.05f, (Render.getStringHeight() - Render.getStringAscent() / 2)), Render.stringDimensions(text).scaleClone(1.1f));

            Render.setColor(new Color(201, 191, 1));
            Render.drawText(text, textPos.addClone(1, 1));

            Render.setColor(new Color(255, 242, 0));
            Render.drawText(text, textPos);
        }
    }

    private static void enableBankInterface() {
        BankingHandler.init();

        RightClick.render = false;
        renderBank = true;
    }

    private static void disableBankInterface() {
        renderBank = false;
    }

    public static void closeBank() {
        Main.sendPacket("gc" + Player.name);
    }

    public static void enableShop(String args) {
        Shop.offeredItems.clear();
        Shop.prices.clear();

        args = args.substring(5);

        String[] verbs = args.split(";");
        Shop.shopVerb = verbs[0];
        Shop.inventoryVerb = verbs[1];
        Shop.showPrices = verbs[2].equals("true");

        renderShop = true;
    }

    public static void closeShop() {
        Shop.offeredItems.clear();
        Shop.prices.clear();
        renderShop = false;

        Main.sendPacket("lf" + Player.name);
    }

    public static void openGUI(String message) {
        String[] args = message.split(";");

        switch (args[0]) {
            case "bankon":
                GUI.enableBankInterface();
                break;
            case "bankoff":
                GUI.disableBankInterface();
                extraCommands.clear();
                break;
            case "shop":
                GUI.enableShop(message);
                break;
            case "shopoff":
                GUI.closeShop();
                extraCommands.clear();
                break;
            case "shopextra":
                extraCommands.add(message.replace("shopextra;", ""));
                break;
        }
    }

    public static void drawInvTimer(int x, int y, InventoryManager.InvTimer invTimer) {
        float percent = (invTimer.getFinishTime() - System.currentTimeMillis()) / (float) invTimer.getDuration();

        Vector2 start = getGridPosition(x, y).addClone(0, intBoxSize - 5);

        Render.setColor(Color.BLUE);
        Render.drawBorderedRect(start, new Vector2(intBoxSize * percent, 5), 1);
    }
}
