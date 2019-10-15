package com.Game.Items.RawResource;

import com.Game.Items.Item;
import com.Game.Items.ItemList;

public class StringItem extends Item {
    public StringItem(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);

        options.add("Craft Bow String");
    }

    public void OnClick(int index) {
        convert(3, 1, ItemList.bowString);
    }
}
