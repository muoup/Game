package com.Game.Object.Utilities;

import com.Game.GUI.GUI;
import com.Game.Object.GameObject;

public class StorageChest extends GameObject {
    public StorageChest(int x, int y) {
        super(x, y);
        image = getImage("chest.png");
        maxDistance = 72;
        setScale(64, 64);
    }

    public boolean onInteract() {
        GUI.enableBankInterface();
        return true;
    }
}
