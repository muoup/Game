package com.Game.GUI.GUIWindow;

import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.util.ArrayList;

// TODO: Add GUI Scaling
// TODO: Make the GUIElements render partially when they are at the side of the GUI

/**
 * New Implementation of a GUI System.
 * Create A GUIWindow and add GUIElements that it will Render
 */
public class GUIWindow {
    public static final GUIWindow emptyGUI = new GUIWindow();
    private Vector2 startOffset;
    private Vector2 size;
    private Color color;
    private ArrayList<GUIElement> elements;
    protected final int padding = 16;

    public GUIWindow() {
        startOffset = Vector2.zero();
        size = Vector2.zero();
        elements = new ArrayList<GUIElement>();
    }

    /**
     * Sets the position and size of the GUI Window
     * @param size Size of GUI
     * @param startOffset Position of GUI Relative to (0, 0)
     */
    public void setRestraints(Vector2 size, Vector2 startOffset) {
        this.size = size;
        this.startOffset = startOffset;
    }

    public boolean isEmpty() {
        return (size.equalTo(Vector2.zero()));
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    private void size(float x, float y) {
        this.size = new Vector2(x, y);
    }

    private void position(float x, float y) {
        this.startOffset = new Vector2(x, y);
    }

    public void setSize(float xPercentage, float yPercentage) {
        Vector2 screenSize = Settings.curResolution();
        size(screenSize.x * xPercentage / 100, screenSize.y * yPercentage / 100);
    }

    public void setPosition(float xPercentage, float yPercentage) {
        Vector2 screenSize = Settings.curResolution();
        position(screenSize.x * xPercentage / 100, screenSize.y * yPercentage / 100);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Vector2 offset(Vector2 position) {
        return position.addClone(startOffset).offsetClone(padding);
    }

    public Vector2 getPosition() {
        return startOffset;
    }

    public Vector2 getSize() {
        return size;
    }

    public void addElement(GUIElement element) {
        elements.add(element);
    }
    void setPosition(Vector2 startOffset) {
        this.startOffset = startOffset;
    }

    public void tick() {
        Render.setColor(color);
        Render.drawBorderedRect(startOffset, size);

        for (GUIElement e : elements) {
            e.update(this);
            e.render(this);
        }

        update();
        render();
    }

    protected void update() {

    }

    protected void render() {
    }

    public GUIElement inElement() {
        for (GUIElement element : elements) {
            if (Input.mouseInRect(element.position, element.size)) {
                return element;
            }
        }

        return null;
    }
}