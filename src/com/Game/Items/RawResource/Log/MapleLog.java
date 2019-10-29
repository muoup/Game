package com.Game.Items.RawResource.Log;

import com.Game.Items.ItemList;

public class MapleLog extends Log {
    public MapleLog(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);

        bow = ItemList.mapleBow;
        arrowShaft = 35;
        setTier(50);
    }
}
