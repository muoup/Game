package com.Game.GUI.Banking;

import com.Game.GUI.GUIWindow.BankItemSlot;
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
    public static ArrayList<BankItemSlot> itemStacks;

    static int xMax;

    public BankingGUI() {
        itemStacks = new ArrayList<BankItemSlot>();

        setColor(new Color(166, 134, 76));
        setPosition(5, 5);
        setSize(65, 40);

        xMax = (int) (getSize().x - padding) / 64;
    }


    protected void render() {
        for (int i = 0; i < itemStacks.size(); i++) {
            BankItemSlot slot = itemStacks.get(i);

            int x = i % xMax;
            int y = i / xMax;

            slot.render(this, x * 64 + padding, y * 64 + padding);
        }
    }

    public void addItemSlot(ItemStack stack) {
        BankItemSlot slot = new BankItemSlot(stack);

        itemStacks.add(slot);
    }
}
