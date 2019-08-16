package com.Game.listener;

import com.Game.GUI.Chatbox.ChatBox;
import com.Util.Math.Vector2;

import java.awt.event.*;

public class Input implements KeyListener, MouseListener, MouseMotionListener {

    public static boolean[] keys = new boolean[65535];
    public static boolean[] keysDown = new boolean[65535];
    public static boolean[] mouse = new boolean[4];
    public static boolean[] mouseDown = new boolean[4];
    public static Vector2 mousePosition = Vector2.zero();

    public static boolean GetKey(int code) {
        return keys[code];
    }

    public static boolean GetKeyDown(int code) {
        boolean ret = keysDown[code];

        if (ret)
            keysDown[code] = false;

        return ret;
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
        ChatBox.type((keys[KeyEvent.VK_BACK_SPACE]) ? "bs" : Character.toString(e.getKeyChar()));
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Input.keys[e.getKeyCode()] = true;
        Input.keysDown[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Input.keys[e.getKeyCode()] = false;
        Input.keysDown[e.getKeyCode()] = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouse[e.getButton()] = true;
        mouseDown[e.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
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
}
