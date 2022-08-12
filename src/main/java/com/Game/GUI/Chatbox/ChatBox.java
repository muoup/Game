package com.Game.GUI.Chatbox;

import com.Game.Entity.Player;
import com.Game.GUI.GUI;
import com.Game.Main.Main;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class ChatBox {
    // Static Variables - These can be changed in a future settings menu?
    // Change these if needed.
    public static int maxMessages = 50; // self-explanatory
    private static float barSensitivity = 1f; // How much the bar needs to be scrolled (greater is less scrolling).
    private static float heightOffset = 50f; // How far up the GUI should be pushed.
    public static Font textFont; // see initialization
    public static Color chatColor = new Color(175, 129, 34, 191);

    // There is not harm in touching these but it will not change it.
    // They are changed every render to help with cleaning up math when rendering the chatbox.
    private static Vector2 size = Vector2.zero(); // Size of state for below variables.
    protected static Vector2 bSize; // Chat Box scrollbar size.
    protected static Vector2 gSize; // Chat Box GUI size.
    private static Vector2 mSize; // Chat Message Bar size.
    private static Vector2 gPos; // The position of the chat box.
    private static Vector2 bPos; // The position of the char bar.
    private static Vector2 mPos; // The position of the chat message bar.
    private static float height = 0; // The height of the chat gui minus chat message bar.

    // Dynamic Variables - These change depending on player interaction with the GUI.
    private static Vector2 initMouse = Vector2.zero();
    private static int initScroll = 0; // This is used for gauging mouse movement for bar movement
    public static int scroll = 0; // The current bar movement down the GUI
    public static int distScroll = 0; // The total height of the messages at the current moment.
    public static float maxScroll = 1; // The maximum scroll for the scroll bar.
    public static ArrayList<Message> messages; // All of the messages the player has received / written.

    // These really should not be touched because they are used for a purpose.
    private static boolean onStartup = true; // Initialization after graphic initialization
    private static boolean moveBar = false; // Is the player moving the scroll bar?
    public static boolean typing = false; // Is the player typing?
    private static String type = ""; // The player's current message within the message bar.
    private static boolean scrollDown = false;
    public static String tag;

    public static float getPadding() {
        return Render.getStringHeight() * 0.5f;
    }

    public static void init() {
        messages = new ArrayList();
        textFont = new Font("TimesRoman", Font.PLAIN, 18);
    }

    public static void startUp() {
        sendMessage("Welcome " + Player.name + ", to the Game!");
    }

    public static void render() {
        if (onStartup) {
            startUp();
            onStartup = false;
        }

        Render.setFont(textFont);

        Render.setColor(chatColor);
        Render.drawBorderedRect(gPos, gSize);

        updateBar();
        renderBar();

        updateText();
        renderText();

        updateChatBar();
        renderChatBar();
    }

    public static void update() {
        Render.setFont(textFont);
        size = GUI.GUIEnd().subtractClone(GUI.GuiPos);
        mSize = new Vector2(size.x * 1.25f, size.y * 0.125f);
        bSize = new Vector2(size.x * 0.1f, size.y * 1.25f - mSize.y);
        gSize = new Vector2(size.x * 1.25f, size.y * 1.25f - mSize.y);
        bPos = new Vector2(gSize.x - size.x * 0.125f + heightOffset, Settings.screenSize().y - gSize.y - heightOffset);
        gPos = new Vector2(heightOffset / 2, bPos.y);
        mPos = gPos.addClone(0, gSize.y - mSize.y);
        height = Math.min(gSize.y, gSize.y * (gSize.y - mSize.y) / distScroll);
        maxScroll = bSize.y;

        if (scrollDown) {
            scroll = (int) (maxScroll - height);
            scrollDown = false;
        }
    }

    public static void renderBar() {
        Render.setColor(new Color(40, 38, 34));
        Render.drawBorderedRect(bPos, bSize);

        Render.setColor(moveBar && Input.GetMouse(1) ? new Color(181, 160, 108, 191) :
                new Color(181, 151, 57, 191));
        Render.drawBorderedRect(bPos.addClone(0, scroll), new Vector2(bSize.x, height));
    }

    public static void updateBar() {
        if (inBar() && Input.GetMouse(1)) {
            if (initMouse.isZero()) {
                initMouse = Input.mousePosition.clone();
                initScroll = scroll;
            }

            moveBar = true;
        }

        if (!Input.GetMouse(1) && !initMouse.isZero()) {
            initMouse = Vector2.zero();
            moveBar = false;
        }

        if (moveBar)
            scroll = initScroll + (int) (Input.mousePosition.y - initMouse.y);

        if (scroll < 0)
            scroll = 0;

        if (scroll > maxScroll - height)
            scroll = (int) (maxScroll - height);
    }

    /**
     * Renders the Chat Box text box. Handles drawing only the text that will show up in the ChatBox
     * depending on how the player has scrolled through the Chat Box.
     *
     * If you need me to document this for something, tell me because I will.
     */
    public static void renderText() {
        Render.setColor(Color.BLACK);
        Render.setFont(textFont);

        Vector2 v1 = gPos.addClone(gPos.x * 0.5f, gPos.x * 0.25f)
                .subtractClone(0, (distScroll == 0) ? 0 : (scroll * distScroll) / gSize.y);

        for (int i = 0; i < messages.size(); i++) {
            Message msg = messages.get(i);
            float y2 = v1.y + msg.getHeight();

            if (y2 > gPos.y && y2 < gPos.y + gSize.y) {
                float dHeight = Math.max(0, gPos.y - v1.y);
                Render.drawCroppedText(msg.message, new Vector2(v1.x, v1.y), new Vector2(0, dHeight));
            } else if (y2 > gPos.y + gSize.y) {
                float dHeight = Math.max(0, y2 - (gPos.y + gSize.y));

                if (dHeight <= 0 || dHeight > msg.getHeight())
                    break;

                Render.drawCroppedText(msg.message, new Vector2(v1.x, v1.y), Vector2.zero(), new Vector2(0, dHeight));
                break;
            }

            v1.y += msg.getHeight() + getPadding();
        }
    }

    public static void updateText() {

    }

    public static void renderChatBar() {
        Render.setColor(chatColor.darker());
        Render.drawBorderedRect(mPos, mSize);

        // The space above and below the message bar text translated to the sides as well.
        float offset = Render.getStringAscent() / 2;//(mSize.y - Render.getStringHeight()) / 2;

        // Location of the message bar text.
        Vector2 tDraw = mPos.addClone(offset, offset * 0.5f);

        Render.setColor(Color.BLACK);
        Render.setFont(textFont);
        float xDif = Math.max(0, (Render.getStringWidth(tag + type)) - (mSize.x - offset * 2) + 1);

        Render.drawCroppedText(tag + type + ((typing) ? "|" : ""), tDraw.subtractClone(xDif, 0), new Vector2(xDif, 0));
    }

    public static void updateChatBar() {
        if (Input.GetMouse(1) && inType()) typing = true;
        else if (Input.GetMouse(1) && !inType()) typing = false;

        if (Input.GetKey(KeyEvent.VK_ENTER) && !type.isEmpty()) {
            sendPublicMessage("[" + Player.name + "] " + type);
            type = "";
        }
    }

    public static void type(String s) {
        if (!typing)
            return;

        if (s.equals("bs")) { // Backspace
            type = type.substring(0, Math.max(0, type.length() - 1));
        } else if (s.equals("es")) {
            typing = false;
        } else if (s.length() != 2) {
            type += s;
        }
    }

    private static boolean inBar() {
        return Input.mousePosition.compareTo(bPos.addClone(0, scroll)) == 1 && bPos.addClone(size.x * 0.125f, height + scroll).compareTo(Input.mousePosition) == 1;
    }

    private static boolean inType() {
        return (Input.mousePosition.compareTo(mPos) == 1)
                && (Input.mousePosition.compareTo(mPos.addClone(mSize)) == -1);
    }

    /**
     * Client-side form of sendPublicMessage(String message), does not send the message
     * to all other players in the game. Used either by the server to display messages
     * on player clients or for in-game Chat Box implementations such as Objects/Item requirement
     * messages.
     * @param message String form of the message
     */
    public static void sendMessage(String message) {
        if (message == null)
            return;

        if (message.length() < 2)
            return;

        Message msg = new Message(message, Color.BLACK);

        /*
            Basic spam filtering, prevents the chat box from sending multiple of the same message.
            Also use full so that the requirement message does not repeat over and over again
            if you are trying to use a game object.
         */
        if (!messages.isEmpty())
            if (messages.get(messages.size() - 1).message.replace("/n", " ").equals(message))
                return;

        scrollDown = (scroll == (int) (maxScroll - height)) || (height == bSize.y);

        if (Main.graphics == null)
            System.err.println("The graphics component is null!");

        if (messages.size() > maxMessages) {
            distScroll -= messages.get(0).getHeight() + getPadding();
            messages.remove(0);
        }

        Render.setFont(textFont);
        messages.add(messages.size(), msg);
        distScroll += msg.getHeight() + getPadding();
    }

    /**
     * Sends a chat box message to all other players on the server, only used right-now for
     * player messages. There is no call to sendMessage(String message) because it pings to
     * the server and then back to all of the clients. sendMessage(String message) contains
     * anti-spam protection so player's Chat Boxes will not be filled with player chat if
     * someone keeps typing in the same message over and over again.
     * @param message
     */
    public static void sendPublicMessage(String message) {
        Message msg = new Message(message, Color.BLACK);

        scrollDown = (scroll == (int) (maxScroll - height)) || (height == bSize.y);

        if (msg.rawMessage.length() > 2)
            if (msg.rawMessage.startsWith("::")) {
                Commands.onCommand(msg);
                return;
            }

        // Send message to server
        Main.sendPacket(Main.messageCode + msg.message);
    }

    public static int getMaxScroll() {
        return (int) (Message.height() / barSensitivity);
    }
}