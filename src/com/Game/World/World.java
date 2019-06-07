package com.Game.World;

import com.Util.Math.Vector2;

public class World {

    private static World[] worlds = {
            new World(5000, 5000) // Normal Overworld
    };

    public static World curWorld = worlds[0];

    public Vector2 size;
    public Vector2 offset;

    public World(int x, int y) {
        size = new Vector2(x, y);
        offset = Vector2.zero();
    }

    public void changeOffset(Vector2 change) {
        offset.add(change);
    }

    public void setOffset(Vector2 set) {
        offset = set;
    }
}
