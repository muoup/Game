package com.Game.GUI.GUIWindow.Components;

import com.Game.GUI.GUIWindow.BasicGUIWindow;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;

import java.awt.*;

public class CloseButton {
    private Vector2 size;
    private Vector2 position;
    private BasicGUIWindow window;

    public CloseButton(Vector2 size, BasicGUIWindow window) {
        this.size = size;
        this.window = window;

        calculatePosition();
    }

    public void render() {
        Render.setColor(Color.RED);
        Render.drawBorderedRect(position, size);
    }

    public void update() {
        if (Input.mouseInRect(position, size))
            if (Input.GetMouseDown(1))
                window.closeWindow();
    }

    private void calculatePosition() {
        // Makes sure that the position of the close window is going to put it
        // so that it fits in the top right of the window.
        position = new Vector2(window.getBeginPos().x + window.getSize().x,
                                window.getBeginPos().y);
    }
}
