package com.Game.GUI.Inventory;

import com.Game.GUI.GUI;
import com.Game.GUI.RightClick;
import com.Game.Items.ItemData;
import com.Game.Main.Main;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.image.BufferedImage;

public class InventoryDrag {
    public static ItemData itemDrag; // The item that is being dragged by the mouse, after the mouse is released, set that state space to the item stack.
    private static int inventoryIndex = -1; // The space that the item left when being dragged
    private static Vector2 initMousePos = Vector2.zero();
    private static boolean drag = false;
    private static boolean click = true;
    private static BufferedImage itemDragImage = null;
    private static float timer = 0f;

    public static void init() {
        itemDrag = new ItemData();
    }

    public static void render() {
        if (RightClick.render || RightClick.coolDown > 0)
            return;

        if (itemDrag != null && drag && !click) {
            // When the player attempts to drag the item, draw the image.
            InventoryManager.useIndex = -1;
            Vector2 rectPos = Input.mousePosition.subtractClone(itemDragImage.getWidth() / 2, itemDragImage.getWidth() / 2);

            Render.drawImage(itemDragImage, rectPos);

            if (itemDrag.getAmount() > 1) {

                String text = GUI.formatAmount(itemDrag.getAmount());

                Render.setFont(Settings.itemFont);

                Render.drawText(text,
                        rectPos.addClone(new Vector2(GUI.intBoxSize - Settings.sWidth(text) - 4, GUI.intBoxSize - 4)));
            }

        } else if (itemDrag.notEmpty() && !drag && GUI.inGUI() && !click) {
            int index = getInventoryIndex();

            // Swap the two spaces in case there is already an item in the state slot.
            InventoryManager.swapSlots(inventoryIndex, index);

            resetVariables();
        } else if (itemDrag.notEmpty() && !drag && !GUI.inGUI() && !click) {
            // When the player attempts to drag the item outside the state
            InventoryManager.inventory[inventoryIndex] = itemDrag;
            itemDrag = null;
            click = true;

            resetVariables();
        } else if (itemDrag.notEmpty() && !drag && click) {
            // When the player clicks on an item
            if (InventoryManager.useIndex >= 0) {
                InventoryManager.use(inventoryIndex);
                InventoryManager.useIndex = -1;
            } else {
                InventoryManager.ClickIdentities(inventoryIndex);
            }
            resetVariables();
        } else if (inventoryIndex != -1 && !drag) {
            resetVariables();
        }
    }

    public static void update() {
        if (RightClick.render)
            return;

        drag = (drag) || Input.GetMouseDown(1);

        if (!Input.GetMouse(1))
            drag = false;

        if (drag && GUI.inGUI() && inventoryIndex == -1) {
            inventoryIndex = getInventoryIndex();
            itemDrag = InventoryManager.getStack(inventoryIndex);
            itemDragImage = Render.getScaledImage(itemDrag.getImage(), GUI.invSize);
            initMousePos = Input.mousePosition.clone();
        }

        if (itemDrag.notEmpty() && (timer > 0.35f || Vector2.distance(initMousePos, Input.mousePosition) > 16)) {
            click = false;
            InventoryManager.draggedIndex = inventoryIndex;
        }

        if (inventoryIndex != -1) {
            timer += Main.dTime();
        }
    }

    private static void resetVariables() {
        click = true;
        itemDrag = new ItemData();
        inventoryIndex = -1;
        timer = 0f;
        InventoryManager.draggedIndex = -1;
    }

    private static int getInventoryIndex() {
        Vector2 mousePos = Input.mousePosition.subtractClone(GUI.GuiPos);

        int x = (int) mousePos.x / GUI.intBoxSize;
        int y = (int) mousePos.y / GUI.intBoxSize;

        return x + y * 4;
    }
}