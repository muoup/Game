package com.Game.listener;

public enum KeyState {
    released,
    pressed,
    inPress;

    public boolean pressed() {
        return equals(pressed) || equals(inPress);
    }
}
