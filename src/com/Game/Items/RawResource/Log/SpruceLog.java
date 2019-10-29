package com.Game.Items.RawResource.Log;

import com.Game.Items.ItemList;

public class SpruceLog extends Log {
    public SpruceLog(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);

        bow = ItemList.spruceBow;
        arrowShaft = 35;
        setTier(40);
    }
}
