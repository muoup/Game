package com.Game.Object;

import com.Util.Math.Vector2;
import com.Util.Other.Sprite;

import java.util.ArrayList;
import java.util.Arrays;

public class UsableGameObject extends GameObject {
    public static UsableGameObject empty = new UsableGameObject(Vector2.zero(), null);
    public ArrayList<String> options;

    public UsableGameObject(Vector2 position, Sprite image) {
        super(position, image);
        options = new ArrayList<String>();
    }

    public void addOptions(String... opts) {
        options.addAll(Arrays.asList(opts));
    }

    public void onOption(int option) {

    }

    public void onRightClick() {

    }
}
