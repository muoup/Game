package com.Game.Items.RawResource.Log;

import com.Game.Items.ItemList;

public class AshLog extends Log {
    public AshLog(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);

        bow = ItemList.ashBow;
        arrowShaft = 25;
    }
}
