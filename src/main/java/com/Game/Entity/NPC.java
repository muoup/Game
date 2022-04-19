package com.Game.Entity;

import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Game.World.World;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Sprite;

import java.awt.image.BufferedImage;

public class NPC {

    protected BufferedImage image;
    public Vector2 position;

    public NPC(Vector2 position, Sprite image) {

        this.position = position;
        this.image = image.getImage();

        MethodHandler.npcs.add(this);
    }

    public void setImage(String imageName) {
        this.image = Main.main.getImageFromRoot("Entities/NPCs/" + imageName);
    }

    public void render() {
        if (image == null)
            return;

        if (Render.onScreen(position, image)) {
            Render.drawImage(image, position.subtractClone(World.offset).subtractClone(image.getWidth() / 2, image.getHeight() / 2));
        }
    }
}
