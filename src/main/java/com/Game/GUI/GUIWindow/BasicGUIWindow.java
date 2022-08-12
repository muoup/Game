package com.Game.GUI.GUIWindow;

import com.Game.GUI.GUI;
import com.Game.Items.ItemData;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.util.ArrayList;

public class BasicGUIWindow {
    protected static Vector2 beginPos = Settings.screenSize().scaleClone(0.25f);
    protected static Vector2 size = Settings.screenSize().scaleClone(0.5f);
    protected static Vector2 itemScale = new Vector2(GUI.intBoxSize);

    public static void init() {

    }

    /**
     * Do not override, this is the render + update method for the superclass.
     */
    public static void tick() {

    }

    public static void render() {

    }

    public static void renderCloseButton() {
        Render.setColor(Color.RED);
        Render.drawBorderedRect(beginPos.addClone(size.x - GUI.intBoxSize / 2, 0), new Vector2(GUI.intBoxSize / 2));
    }

    public static void renderBackground() {
        // Draw basic stuff for every shop such as background and items.
        Render.setColor(new Color(182, 124, 45));
        Render.drawBorderedRect(beginPos, size);
    }

    public static void renderItems(Vector2 offset, int padding, ArrayList<ItemData> items) {
        int maxRow = (int) ((size.x - padding) / (padding + itemScale.x));

        for (int i = 0; i < items.size(); i++) {
            int x = i % maxRow;
            int y = i / maxRow;

            renderItemSlot(items.get(i), new Vector2(x, y), itemScale, padding, false);
        }
    }

    public static void renderItemSlot(ItemData stack, Vector2 gridPosition, Vector2 scale, int padding, boolean ignoreAmount) {
        String text = GUI.formatAmount(stack.getAmount());

        Vector2 rectPos = beginPos.addClone(padding + (padding + GUI.intBoxSize) * gridPosition.x, padding + (padding + GUI.intBoxSize) * gridPosition.y);
        Vector2 textPos = rectPos.addClone(new Vector2(GUI.intBoxSize - Settings.sWidth(text) - 4, GUI.intBoxSize - 4));

        // Renders The Item Image
        Render.setColor(Color.BLACK);
        Render.drawRectOutline(rectPos, scale);
        Render.drawImage(Render.getScaledImage(stack.getImage(), scale), rectPos);

        // Renders the Item's Amount Text
        Render.setColor(Color.BLACK);
        Render.setFont(Settings.itemFont);
        Render.drawText(text, textPos.addClone(1, 0));
    }

    public static void update() {

    }

    public static Vector2 getBeginPos() {
        return beginPos;
    }

    public static Vector2 getSize() {
        return size;
    }

    public static Vector2 getItemScale() {
        return itemScale;
    }

    public static void closeWindow() {

    }
}
