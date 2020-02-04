package com.Game.Object;

import java.util.ArrayList;
import java.util.Arrays;

public class UsableGameObject extends GameObject {
    public static UsableGameObject empty = new UsableGameObject(0, 0);
    public ArrayList<String> options;

    public UsableGameObject(int x, int y) {
        super(x, y);
        options = new ArrayList<String>();
    }

    public void addOptions(String... opts) {
        options.addAll(Arrays.asList(opts));
    }

    public void onOption(int option) {

    }

    public void onRightClick() {

    }

    public void loseFocus() {

    }
}
