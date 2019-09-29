package com.Game.GUI.GUIWindow;

@FunctionalInterface
public interface ButtonPress {
    /**
     * When a button is pressed, calls a method specified by a button.
     * Use a lambda ((params) -> methodName()) to get the button to call
     * the method when pressed.
     */
    public abstract void run();
}
