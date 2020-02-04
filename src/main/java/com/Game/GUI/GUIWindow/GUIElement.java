package com.Game.GUI.GUIWindow;

import com.Game.listener.Input;
import com.Util.Math.Vector2;

/*
 * GUIElement is a placeholder parent class so that the GUIWindow can hold an
 * array list of GUIElements to render.
 */
public class GUIElement {
    public Vector2 position = Vector2.zero();
    public Vector2 size = Vector2.zero();

    public void render(GUIWindow window) {

    }

    public void update(GUIWindow window) {

    }

    protected boolean isHover(GUIWindow window) {
        return Input.mousePosition.greaterThan(window.offset(position)) &&
                window.offset(position.addClone(size)).greaterThan(Input.mousePosition);
    }
}
