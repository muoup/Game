package com.Game.Object;

import com.Game.Items.ItemData;
import com.Game.Main.MethodHandler;
import com.Game.World.World;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GroundItem {
    public Vector2 position;
    public ArrayList<ItemData> itemStacks;
    public int randomToken;

    public GroundItem(Vector2 position) {
        this.position = position;
        this.itemStacks = new ArrayList<>();
    }

    public static GroundItem mouseOver() {
        for (GroundItem item : MethodHandler.groundItems) {
            if (Vector2.distance(item.position.subtractClone(World.offset), Input.mousePosition) < Settings.maxPickupDistance) {
                return item;
            }
        }

        return null;
    }

    public void renderStack() {
        BufferedImage stackImage = getStackImage();

        if (Render.onScreen(position, stackImage)) {
            Render.drawImage(stackImage, position.subtractClone(World.offset).subtractClone(stackImage.getWidth() / 2, stackImage.getHeight() / 2));
        }
    }

    public BufferedImage getStackImage() {
        if (itemStacks.size() == 0)
            return new BufferedImage(0, 0, BufferedImage.TYPE_INT_ARGB);

        return itemStacks.get(0).getImage();
    }

    public void addStack(ItemData data) {
        itemStacks.add(data);
    }

    public void removeStack(int index) {
        itemStacks.remove(index);
    }

    public void modifyStack(int index, ItemData newData) {
        itemStacks.set(index, newData);
    }

    public void set(GroundItem groundItem) {
        itemStacks = new ArrayList<>();
        itemStacks.addAll(groundItem.itemStacks);
        randomToken = groundItem.randomToken;
        position = groundItem.position.clone();
    }
}
