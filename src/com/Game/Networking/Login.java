package com.Game.Networking;

import com.Game.Main.Main;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Login {
    private static String nameField = "";
    private static String passwordField = "";
    private static int selected;
    private static Font loginFont;
    private static Color barColor;
    private static Vector2 scale;
    private static Vector2 res;
    private static Vector2 namePos;
    private static Vector2 passPos;
    private static Vector2 center;
    private static Vector2 buttonSize;
    private static Vector2 loginPos;
    private static Vector2 registerPos;
    private static final int nameLimit = 15;
    private static final int passLimit = 15;

    public static void init() {
        loginFont = Main.getFont("login-text.ttf", 25, Font.PLAIN);
        barColor = new Color(166, 135, 61);
        selected = 0;
        res = Settings.curResolution();
        center = res.scaleClone(0.5f);
        scale = new Vector2(res.x / 4, res.y / 25);
        namePos = center.subtractClone(scale.x / 2, scale.y * 1.2f);
        passPos = center.subtractClone(scale.x / 2, -scale.y * 1.2f);
        buttonSize = new Vector2(center.x / 5, center.y / 10);
        loginPos = center.addClone(-center.x / 3, center.y / 3.5f);
        registerPos = center.addClone(center.x / 3 - buttonSize.x, center.y / 3.5f);
    }

    public static void update() {
        if (Input.GetMouseDown(1)) {
            selected = 0;
            if (Input.mouseInRect(namePos, scale))
                selected = 1;
            else if (Input.mouseInRect(passPos, scale))
                selected = 2;
            else
                selected = 0;

            if (!nameField.isBlank() && !passwordField.isBlank()) {
                if (Input.mouseInRect(loginPos, buttonSize))
                    Main.serverConnect(nameField, passwordField, 0);
                else if (Input.mouseInRect(registerPos, buttonSize))
                    Main.serverConnect(nameField, passwordField, 1);
            }
        }
        if (selected > 0) {
            if (Input.GetKeyDown(KeyEvent.VK_TAB)) {
                selected = (selected == 1) ? 2 : 1;
            }
        }

    }

    public static void resetLogin() {
        passwordField = "";
    }

    public static void render() {
        Render.setColor(new Color(131, 199, 192));
        Render.setFont(loginFont);
        Render.drawRectangle(Vector2.zero(), Settings.curResolution());

        Render.setColor(barColor.brighter());

        Render.drawBorderedBounds(center.scaleClone(0.5f), center.scaleClone(1.5f));

        // Username Field
        Render.setColor(barColor);

        Render.drawBorderedRect(namePos, scale);

        // Password Field
        Render.setColor(barColor);

        Render.drawBorderedRect(passPos, scale);

        Render.setColor(Color.GREEN.darker());

        // Render login button
        Render.drawBorderedRect(loginPos, buttonSize);

        Render.setColor(Color.CYAN.darker());

        // Render sign-up button
        Render.drawBorderedRect(registerPos, buttonSize);

        // Render Labels
        Render.setColor(Color.BLACK);
        Render.drawText("Username:", center.subtractClone(scale.x / 2, scale.y * 1.4f));
        Render.drawText("Password:", center.subtractClone(scale.x / 2, -scale.y * 1f));
        Render.drawText("Sign-in", center.addClone(-center.x / 3 + Render.getStringWidth("-"), center.y / 3.5f + Render.getStringHeight() * 1.1f));
        Render.drawText("Register", center.addClone(center.x / 3 - buttonSize.x + Render.getStringWidth("-"), center.y / 3.5f + Render.getStringHeight() * 1.1f));

        // Render Fields
        Render.drawText(nameField + ((selected == 1) ? "|" : "") , namePos.addClone(Render.getStringWidth("-"), Render.getStringHeight() * 0.9f));
        Render.drawText(toPassword(passwordField) + ((selected == 2) ? "|" : "") , passPos.addClone(Render.getStringWidth("-"), Render.getStringHeight() * 0.9f));
    }

    public static void onType(String type) {
        switch (selected) {
            case 0:
                return;
            case 1:
                nameField += type;
                if (type == "bs") {
                    nameField = nameField.substring(0, Math.max(0, nameField.length() - 3));
                }
                if (nameField.length() > nameLimit) {
                    nameField = nameField.substring(0, 15);
                }
                break;
            case 2:
                passwordField += type;
                if (type == "bs") {
                    passwordField = passwordField.substring(0, Math.max(0, passwordField.length() - 3));
                }
                if (passwordField.length() > passLimit) {
                    passwordField = passwordField.substring(0, 15);
                }
                break;
            default:
                System.err.println("the login screen has malfunctioned");
        }
    }

    public static String toPassword(String password) {
        String ret = "";
        for (int i = 0; i < password.length(); i++)
            ret += "*";
        return ret;
    }
}
