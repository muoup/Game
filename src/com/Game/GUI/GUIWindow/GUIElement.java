package com.Game.GUI.GUIWindow;

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
}
