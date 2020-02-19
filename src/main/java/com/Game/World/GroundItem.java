package com.Game.World;

import com.Game.Items.ItemStack;
import com.Game.Main.MethodHandler;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.util.ArrayList;

public class GroundItem {
    public Vector2 position;
    public ArrayList<ItemStack> stack;
    private Image topImage;
    private static final float maxDistance = 256f;
    private long time = 0;

    public GroundItem(int x, int y, ArrayList<ItemStack> items) {
        this.position = new Vector2(x, y);
        this.stack = new ArrayList<ItemStack>();
        this.time = System.currentTimeMillis();
        handleStack(items);

        MethodHandler.groundItems.add(this);
    }

    public GroundItem(Vector2 position, ArrayList<ItemStack> items) {
        this.position = position.clone();
        this.stack = new ArrayList<ItemStack>();
        this.time = System.currentTimeMillis();
        handleStack(items);

        MethodHandler.groundItems.add(this);
    }

    public GroundItem(Vector2 position, ItemStack drop) {
        this.position = position.clone();
        this.stack = new ArrayList<ItemStack>();
        this.time = System.currentTimeMillis();
        handleItem(drop);

        MethodHandler.groundItems.add(this);
    }

    public void handleStack(ArrayList<ItemStack> stack) {
        stack.forEach(this::handleItem);
    }

    public void handleItem(ItemStack stack) {
        this.stack.add(stack);
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

    public void addItem(ItemStack item) {
        for (ItemStack s : stack) {
            if (s.getID() == item.getID() && s.getData() == s.getData()) {
                s.amount += item.getAmount();
                return;
            }
        }

        stack.add(item);

        time = System.currentTimeMillis();
    }

    public void addItems(ArrayList<ItemStack> stack) {
        stack.forEach(this::addItem);
    }

    public void update() {
        for (int i = 0; i < stack.size(); i++) {
            if (stack.get(i).getAmount() <= 0)
                stack.remove(i);
        }

        if (stack.size() == 0 || System.currentTimeMillis() - time >= Settings.groundItemDuration)
            MethodHandler.groundItems.remove(this);
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

    public static void createGroundItem(Vector2 position, ArrayList<ItemStack> drops) {
        for (GroundItem i : MethodHandler.groundItems) {
            if (Vector2.distance(i.position, position) < maxDistance) {
                i.addItems(drops);
                return;
            }
        }

        new GroundItem(position, drops);

    }

    public static void createGroundItem(Vector2 position, ItemStack drop) {
        for (GroundItem i : MethodHandler.groundItems) {
            if (Vector2.distance(i.position, position) < maxDistance) {
                i.addItem(drop);
                return;
            }
        }

        new GroundItem(position, drop);
    }

    public String toString() {
        StringBuilder ret = new StringBuilder();
        stack.forEach(ret::append);

        return ret.toString();
    }
}
