package com.Game.listener;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.Main.Main;
import com.Game.Networking.Login;
import com.Util.Math.Rect2;
import com.Util.Math.Vector2;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Objects;

public class Input implements KeyListener, MouseListener, MouseMotionListener {

    public static ArrayList<KeyInstance> keyArray = new ArrayList();
    public static HashSet<Integer> keyPressedArray = new HashSet();
    public static KeyState[] mouse = new KeyState[5];
    public static boolean[] mouseDown = new boolean[5];
    public static Vector2 mousePosition = Vector2.zero();

    public static void init() {

    }

    public static boolean GetKey(int code) {
        for (int i = 0; i < keyArray.size(); i++) {
            KeyInstance instance = keyArray.get(i);
            if (instance.code == code) {
                return instance.state != KeyState.released;
            }
        }

        return false;
    }

    public static boolean GetKeyDown(int code) {
        for (int i = 0; i < keyArray.size(); i++) {
            KeyInstance instance = keyArray.get(i);
            if (instance.code == code) {
                return instance.state == KeyState.pressed;
            }
        }

        return false;
    }

    public static boolean GetMouse(int button) {
        return mouse[button].pressed();
    }

    public static boolean GetMouseDown(int button) {
        return mouse[button] == KeyState.pressed;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyPressedArray.add(e.getKeyCode());

        String input = Character.toString(e.getKeyChar());
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_BACK_SPACE:
                input = "bs";
                break;
            case KeyEvent.VK_ENTER:
                input = "en";
                break;
            case KeyEvent.VK_ESCAPE:
                input = "es";
                break;
        }

        if (!input.matches("^[a-zA-Z0-9s]+$") && !" -_.,/:;!?'".contains(input))
            return;

        ChatBox.type(input);
        Login.onType(input);
    }

    public static void update() {
        try {
            for (int i : keyPressedArray) {
                int id = -1;

                for (int i1 = 0; i1 < keyArray.size(); i1++) {
                    KeyInstance instance = keyArray.get(i1);
                    if (instance.code == i) {
                        id = i;
                        break;
                    }
                }

                // The key has already been pressed the previous frame.
                if (id != -1) {
                    Objects.requireNonNull(find(id)).state = KeyState.inPress;
                }
                // The key was not pressed before this frame.
                else {
                    keyArray.add(new KeyInstance(i, KeyState.pressed));
                }
            }
        } catch (ConcurrentModificationException e) {
            if (Main.client == null)
                return;

            Main.sendPacket("weirdconcurrenterror");
            return;
        }

        for (int i = 0; i < mouseDown.length; i++) {
            boolean mouse = mouseDown[i];

            if (!mouse) {
                Input.mouse[i] = KeyState.released;
                continue;
            }

            if (Input.mouse[i] == KeyState.pressed) {
                Input.mouse[i] = KeyState.inPress;
            } else if (Input.mouse[i] == KeyState.released) {
                Input.mouse[i] = KeyState.pressed;
            }
        }
    }

    private static KeyInstance find(int id) {
        for (KeyInstance key : keyArray) {
            if (key.code == id)
                return key;
        }

        System.err.println("something dun guffed");
        return null;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPressedArray.remove(e.getKeyCode());
        keyArray.removeIf(keyInstance -> keyInstance.code == e.getKeyCode());
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() > 4)
            return;

        mouseDown[e.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() > 4)
            return;

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

    public static boolean mouseInRect(Rect2 rect) {
        return mouseInBounds(rect.getPos(), rect.getEnd());
    }
}

class KeyInstance {
    public final int code;
    public KeyState state;

    public KeyInstance(final int index, KeyState state) {
        this.code = index;
        this.state = state;
    }
}