package com.Game.GUI.Banking;

import com.Game.GUI.GUIWindow.GUIItemSlot;
import com.Game.GUI.GUIWindow.GUIWindow;
import com.Game.Items.ItemStack;

import java.awt.*;
import java.util.ArrayList;

public class BankingGUI extends GUIWindow {
    // TODO: Save bank to server after full client-side implementation
    // TODO: Determine positions for bank item slots to make them render correctly

    /**
     * GUIItemSlots currently being rendered within the bank.
     */
    public static ArrayList<GUIItemSlot> itemStacks;

    static int xMax;

    public BankingGUI() {
        itemStacks = new ArrayList<GUIItemSlot>();

        setColor(new Color(166, 134, 76));
        setPosition(5, 5);
        setSize(65, 40);

        xMax = (int) (getSize().x - padding) / 64;
    }

    public void addItemSlot(ItemStack stack) {
        int length = itemStacks.size();
        int x = length % xMax + 1;
        int y = length / xMax;

        if (x == xMax) {
            x = 0;
            y++;
        }

        GUIItemSlot slot = new GUIItemSlot(stack, x * 60, y * 60);

        itemStacks.add(slot);
        addElement(slot);
    }
}
