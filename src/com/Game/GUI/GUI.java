package com.Game.GUI;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.GUIWindow.GUIWindow;
import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.GUI.Inventory.InventoryDrag;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Shop.Shop;
import com.Game.GUI.Skills.Skills;
import com.Game.GUI.Skills.SkillsManager;
import com.Game.Items.ItemStack;
import com.Game.Main.Main;
import com.Game.Questing.QuestManager;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;

public class GUI {

    private static float coolDown = 0f;
    private static Image[] inventoryOptions;
    public static int select, IntBoxSize, curMain = 0;
    public static Vector2 below, GuiPos, invSize, categorySize;
    public static GUIWindow currentGUI = GUIWindow.emptyGUI;
    private static final String[] invImgNames = {
            "backpack.png",
            "accessories.png",
            "levels.png",
            "questing.png"
    };

    public static Shop currentShop = Shop.empty;

    public static Vector2 GUIEnd() {
        return GuiPos.addClone(4 * IntBoxSize, 5 * IntBoxSize);
    }

    public static boolean inGUI() {
        return Input.mouseInBounds(GuiPos, GUIEnd());
    }

    public static Vector2 getGridPosition(int x, int y) {
        return new Vector2(x, y).scale(GUI.IntBoxSize).add(GuiPos);
    }

    public static void init() {

        Vector2 res = Settings.curResolution();
        inventoryOptions = new Image[invImgNames.length];
        IntBoxSize = (int) (res.x * 0.05f);
        invSize = Vector2.identity(IntBoxSize);
        select = (int) (IntBoxSize * 0.75);
        categorySize = Vector2.identity(select);
        Vector2 mainOffset = new Vector2(IntBoxSize * 0.5f, IntBoxSize * 1.5f);
        GuiPos = res.subtractClone(new Vector2(IntBoxSize * 4f, IntBoxSize * 5.5f)).subtractClone(mainOffset);
        below = GuiPos.addClone(0, IntBoxSize * 5);
        Settings.itemFont = new Font("Arial", Font.PLAIN, (int) Settings.curResolution().x / 75);

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

        InventoryManager.handleInventory();
        AccessoriesManager.handleInventory();

        handleInventory();

        ChatBox.update();
        ChatBox.render();

        currentGUI.tick();

        currentShop.baseRender();
        currentShop.baseUpdate();

        RightClick.update();
        RightClick.render();

        MouseHover.handleHover(curMain);
    }

    public static void update() {
        Vector2 end = below.addClone(new Vector2(IntBoxSize * inventoryOptions.length, IntBoxSize));

        if (Input.mousePosition.compareTo(below) == 1 && end.compareTo(Input.mousePosition) == 1 && Input.GetMouse(1) && !RightClick.render && InventoryDrag.itemDrag.getID() == 0) {
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
                System.out.println(curMain);
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

    public static void drawItem(int x, int y, ItemStack stack) {
        Vector2 rectPos = getGridPosition(x, y);

        Render.setColor(Color.BLACK);
        Render.drawRectOutline(rectPos, GUI.invSize);

        if (stack.getID() == 0)
            return;

        Render.drawImage(Render.getScaledImage(stack.getImage(), GUI.invSize), rectPos);

        // If the stack of items contains more than one item, make sure to write the amount of items at the bottom
        // right corner of the item box.
        if (stack.getAmount() > 1) {
            String text = formatAmount(stack.getAmount());
            Render.setFont(Settings.itemFont);

            Vector2 textPos = rectPos.addClone(new Vector2(GUI.IntBoxSize - Settings.sWidth(text) - 4, GUI.IntBoxSize - 4));

            // Draws the amount of items in the item stack along with a white rectangle on top of it so that
            // the text is easier to read.
//
//            Render.setColor(new Color(255, 255, 255, 150));
//            Render.drawRectangle(textPos.subtractClone(Render.getStringWidth(text) * 0.05f, (Render.getStringHeight() - Render.getStringAscent() / 2)), Render.stringDimensions(text).scaleClone(1.1f));

            Render.setColor(Color.GRAY);
            Render.drawText(text, textPos.addClone(1, 0));

            Render.setColor(new Color(18, 34, 20));
            Render.drawText(text, textPos);
        }
    }
}
