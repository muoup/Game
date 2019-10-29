package com.Game.Items.RawResource.Log;

import com.Game.Items.ItemList;

public class OakLog extends Log {
    public OakLog(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);

        bow = ItemList.oakBow;
        arrowShaft = 20;
        setTier(30);
    }
}
