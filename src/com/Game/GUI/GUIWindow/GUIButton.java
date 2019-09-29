package com.Game.GUI.GUIWindow;

import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;

import java.awt.*;

/**
 * Creates a button to be added to a GUIWindow
 * Set ButtonPress using Lambda notation
 * (() -> methodName())
 * This is similar to how a Thread is created.
 * Server.jar has thread implementations in case the above instructions are unclear.
 */
public class GUIButton extends GUIElement {
    private ButtonPress onClick;
    private String text;
    private Color color;
    private Font font;

    public GUIButton(Vector2 position, Vector2 size, String text) {
        this.position = position;
        this.size = size;
        this.text = text;
        this.color = Color.WHITE;
    }

    public void setOnClick(ButtonPress onClick) {
        this.onClick = onClick;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void update(GUIWindow window) {
        if (Input.mouseInRect(position.addClone(window.getPosition()), size) && Input.GetMouse(1)) {
            onClick.run();
        }
    }

    public void render(GUIWindow window) {
        Render.setColor(color);
        Render.drawBorderedRect(position.addClone(window.getPosition()), size);
        // TODO: Make Text Align with GUIButton
        Render.setColor(Color.BLACK);
        Render.drawText(text, position.addClone(window.getPosition()).addClone(size.x / 2 - Render.getStringWidth(text) / 2, size.y / 2));
    }
}
