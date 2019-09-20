package com.Game.World;

import com.Game.GUI.Inventory.ItemStack;
import com.Game.Main.MethodHandler;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GroundItem {
    public Vector2 position;
    public ArrayList<ItemStack> stack;
    private Image topImage;

    public GroundItem(int x, int y, ArrayList<ItemStack> stack) {
        this.position = new Vector2(x, y);
        this.stack = stack;

        MethodHandler.groundItems.add(this);
    }

    public GroundItem(Vector2 position, ArrayList<ItemStack> stack) {
        this.position = position.clone();
        this.stack = stack;

        MethodHandler.groundItems.add(this);
    }

    public void updateStack() {
        update();
        render();
    }

    public void render() {
        if (stack.size() == 0)
            return;

        topImage = Render.getScaledImage(stack.get(0).getImage(), 64, 64);

        if (Render.onScreen(position, topImage)) {
            Render.drawImage(topImage, position.subtractClone(World.curWorld.offset));
        }
    }

    public void update() {
        for (int i = 0; i < stack.size(); i++) {
            if (stack.get(i).getAmount() <= 0)
                stack.remove(i);
        }

        if (stack.size() == 0)
            MethodHandler.removeGround.add(this);
    }

    public static GroundItem mouseOver() {
        for (GroundItem item : MethodHandler.groundItems) {
            Vector2 iPos = item.position.subtractClone(World.curWorld.offset);

            if (Input.mousePosition.compareTo(iPos) == 1 &&
                    iPos.addClone(64, 64).compareTo(Input.mousePosition) == 1)
                return item;
        }

        return null;
    }
}
