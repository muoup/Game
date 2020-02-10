package com.Game.Object.AreaTeleporter;

import com.Game.Main.Main;
import com.Game.Object.GameObject;

public class Teleporter extends GameObject {
    private int tx, ty, sub;

    public Teleporter(int x, int y, int tx, int ty, int sub) {
        super(x, y);
        this.tx = tx;
        this.ty = ty;
        this.sub = sub;
    }

    public boolean isSuccessful() {
        return true;
    }

    public void unSuccessful() {

    }

    public void onInteraction() {
        if (isSuccessful())
            Main.player.tpToPos(tx, ty, sub);
        else
            unSuccessful();
    }
}
