package com.Game.GUI.GUIWindow;

import com.Game.GUI.GUI;
import com.Game.Items.ItemStack;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

public class BankItemSlot extends GUIItemSlot {

    public BankItemSlot(ItemStack stack) {
        super(stack, Vector2.zero(), GUI.invSize);
    }

    public void update(GUIWindow window) {
        if (getAmount() == 0) {
            window.removeElement(this);
        }
    }

    public void render(GUIWindow window, float x, float y) {
        Vector2 pos = window.offset(new Vector2(x, y));

        if (bordered) {
            Render.drawBorderedRect(pos, renderSize);
        }

        if (renderItem)
            Render.drawImage(Render.getScaledImage(stack.getImage(), renderSize), pos);

        if (renderItem && stack.getAmount() > 1) {
            String text = "" + stack.getAmount();
            Render.drawText(text,
                    pos.addClone(new Vector2(GUI.IntBoxSize - Settings.sWidth(text) - 4, GUI.IntBoxSize - 4)));
        }
    }
}
