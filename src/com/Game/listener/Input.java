package com.Game.listener;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.Networking.Login;
import com.Util.Math.Vector2;

import java.awt.event.*;

public class Input implements KeyListener, MouseListener, MouseMotionListener {

    private static boolean[] keysPressed = new boolean[65535];
    public static KeyState[] keys = new KeyState[65535];
    public static boolean[] mouse = new boolean[5];
    public static boolean[] mouseDown = new boolean[5];
    public static Vector2 mousePosition = Vector2.zero();

    public static void init() {
        for (int i = 0; i < keys.length; i++) {
            keys[i] = KeyState.released;
        }
    }

    public static boolean GetKey(int code) {
        return keys[code].pressed();
    }

    public static boolean GetKeyDown(int code) {
//        KeyState ret = keys[code];
//
//        if (keys[code] == KeyState.pressed)
//            keys[code] = KeyState.inPress;
//
//        return ret == KeyState.pressed;
        return keys[code] == KeyState.pressed;
    }

    public static boolean GetMouse(int button) {
        return mouse[button];
    }

    public static boolean GetMouseDown(int button) {
        boolean ret = mouseDown[button];

        mouseDown[button] = false;

        return ret;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keysPressed[e.getKeyCode()] = true;

        String input = Character.toString(e.getKeyChar());
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_BACK_SPACE:
                input = "bs";
                break;
            case KeyEvent.VK_TAB:
            case KeyEvent.VK_CONTROL:
            case KeyEvent.VK_SHIFT:
                input = "";
                break;
            case KeyEvent.VK_ENTER:
                input = "en";
                break;
            case KeyEvent.VK_ESCAPE:
                input = "es";
                break;
        }
        ChatBox.type(input);
        Login.onType(input);
    }

    public static void update() {
        for (int i = 0; i < keysPressed.length; i++) {
            boolean key = keysPressed[i];

            if (!key) {
                keys[i] = KeyState.released;
                continue;
            }

            if (keys[i] == KeyState.pressed) {
                keys[i] = KeyState.inPress;
            } else if (keys[i] == KeyState.released) {
                keys[i] = KeyState.pressed;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Input.keysPressed[e.getKeyCode()] = false;
        //Input.keys[e.getKeyCode()] = KeyState.released;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() > 4)
            return;
        mouse[e.getButton()] = true;
        mouseDown[e.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() > 4)
            return;
        mouse[e.getButton()] = false;
        mouseDown[e.getButton()] = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (e == null)
            return;

        mousePosition.x = e.getX();
        mousePosition.y = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (e == null)
            return;

        mousePosition.x = e.getX();
        mousePosition.y = e.getY();
    }

    public static boolean mouseInBounds(Vector2 v1, Vector2 v2) {
        return (mousePosition.compareTo(v1) == 1 && mousePosition.compareTo(v2) == -1);
    }

    public static boolean mouseInRect(Vector2 pos, Vector2 size) {
        return mouseInBounds(pos, pos.addClone(size));
    }
}