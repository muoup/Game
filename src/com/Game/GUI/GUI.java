package com.Game.GUI;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.GUI.Inventory.InventoryDrag;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Skills.Skills;
import com.Game.GUI.Skills.SkillsManager;
import com.Game.Main.Main;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;

public class GUI {

    private static float coolDown = 0f;
    private static Image[] inventoryOptions;
    private static String[] invImgNames = {
            "backpack.png",
            "accessories.png",
            "levels.png"
    };
    private static Vector2 below;

    public static int curMain = 0;
    public static int select;
    public static Vector2 GuiPos;
    public static Vector2 invSize;
    public static int IntBoxSize;
    public static Vector2 categorySize;

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
        below = GuiPos.addClone(0,  IntBoxSize * 5);
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
            coolDown -= 1 / Main.fps;
        }

        InventoryManager.handleInventory();
        AccessoriesManager.handleInventory();

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
            default:
                System.err.println("There is something wrong.");
                break;
        }

        ChatBox.update();
        ChatBox.render();

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
}
