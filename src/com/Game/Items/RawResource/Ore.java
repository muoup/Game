package com.Game.Items.RawResource;

import com.Game.Items.Item;
import com.Game.Items.ItemStack;

public class Ore extends Item {
    public Ore(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);
    }

    public void setData(ItemStack stack) {
        if (stack.data == 0) {
            stack.image = getImage(imageName);
        } else {
            stack.image = getImage("ingot_" + imageName);
        }
    }
}
