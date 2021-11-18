package com.Game.Entity;

import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Game.World.World;
import com.Util.Math.Vector2;
import com.Util.Other.Render;

import java.awt.image.BufferedImage;

public class NPC {

    // NOTE: Use id for comparing NPCs rather than comparing the objects themselves.
    public int id;

    protected BufferedImage image;
    public Vector2 position;

    public NPC(int id, int x, int y) {
        this.id = id;

        position = new Vector2(x, y);
        image = null;

        MethodHandler.npcs.add(this);
    }

    public void setImage(String imageName) {
        this.image = Main.main.getImageFromRoot("Entities/NPCs/" + imageName);
    }

    public void render() {
        if (image == null)
            return;

        if (Render.onScreen(position, image)) {
            Render.drawImage(image, position.subtractClone(World.offset));
//            if (Input.GetKeyDown(KeyEvent.VK_E)
//                    && Vector2.distance(Player.position, position) < 150
//                    && !Settings.paused()
//                    && TextBox.noText()
//                    && !ChatBox.typing) {
//                onInteract();
//            }
        }
    }

    public void onInteract() {

    }
}
