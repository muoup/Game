package com.Game.GUI.GUIWindow;

import com.Game.Items.ItemStack;
import com.Game.listener.Input;

public class BankingSlot extends GUIItemSlot {
    GUIWindow window;

    public BankingSlot(GUIWindow window, ItemStack item, float x, float y) {
        super(item, x, y);

        this.window = window;
    }

    public boolean hovered() {
        return Input.mouseInBounds(window.offset(position), window.offset(position.addClone(size)));
    }
}
