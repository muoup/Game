package com.Game.Main;

import com.Game.Entity.Player;
import com.Game.GUI.GUI;
import com.Game.Networking.Client;
import com.Game.Networking.Login;
import com.Game.World.World;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Main extends Canvas {

    private boolean running = false;

    public static double fps = 0;
    public static int deltaTime = 0;
    public static Main main;
    public static JFrame frame;
    public static Dimension screenSize;
    public static Graphics graphics;
    public static final String connectionCode = "69";
    public static final String messageCode = "13";
    public static boolean isConnected = false;

    public static final String ipAddress = "localhost";//"hacksugar.asuscomm.com";
    public static MenuHandler menu;
    public static Client client;

    public static MethodHandler methods;

    private static long updateLength;

    public Main() {
        main = this;

        Vector2 res = Settings.curResolution();
        Dimension screenSize = new Dimension((int) res.x, (int) res.y);

        main.setSize(screenSize);
        main.setPreferredSize(screenSize);
        main.setMaximumSize(screenSize);
        main.setMinimumSize(screenSize);
    }

    public static void main(String[] args) {
        new Main();

        Settings.interpretPreferences();

        frame = new JFrame("Game");
        Input input = new Input();

        frame.add(main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);

        main.running = true;

        Input.init();

        main.init();
        main.updateFrame();
        main.addKeyListener(input);
        main.addMouseListener(input);
        main.addMouseMotionListener(input);
        main.setFocusTraversalKeysEnabled(false);
        main.run();
    }

    public static boolean serverConnect(String username, String password, int loginCode) {
        client = new Client(ipAddress, 3112);
        return client.connect(username, password, loginCode);
    }

    public static void logout() {
        isConnected = false;
        Login.resetLogin();
    }

    public void init() {
        Main.screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Player.init(Settings.playerSpawn, Settings.playerSpeed, Color.GREEN, 2.75f);
        menu = new MenuHandler();

        Settings.npcFont = getFont("npc-text.ttf", 24, Font.BOLD);
        Settings.skillPopupFont = getFont("skill-text.ttf", 16, Font.PLAIN);
        Settings.questFont = getFont("quest-text.ttf", 16, Font.PLAIN);
        Settings.groundFont = getFont("ground-text.ttf", 14, Font.BOLD);

        methods = new MethodHandler();

        initMethods();
    }

    public static double dTime() {
        return deltaTime / 1000f;
    }

    public void initMethods() {
        methods.settings = menu;

        GUI.init();
        Login.init();
    }

    // Used for when frame settings are changed from the settings screen and need to be refreshed.
    public void updateFrame() {
        if (Settings.fullScreen) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            frame.setExtendedState(0);
        }

        frame.setSize((int) Settings.curResolution().x, (int) Settings.curResolution().y);
        frame.setLocationRelativeTo(null);

        Settings.itemFont = new Font("Arial", Font.PLAIN, (int) Settings.screenSize().x / 75);

        initMethods();
    }

    // Calculate fps and run other functions
    public void run() {

        // NOTE: just don't touch any of this, I got this off of StackOverFlow
        double lastFpsTime = 0;
        double lfps = 0;
        long lastLoopTime = System.nanoTime();

        while (running) {
            long optimalTime = 1000000000 / Settings.fpsCap;
            long now = System.nanoTime();
            updateLength = now - lastLoopTime;
            deltaTime = (int) updateLength / 1000000;

            lastLoopTime = now;

            double delta = updateLength / ((double) optimalTime);

            lastFpsTime += updateLength;
            lfps++;

            // Occurs once every second. Used to refresh the frame count per second.
            if (lastFpsTime >= 250000000) {
                lastFpsTime = 0;
                fps = lfps * 4;
                lfps = 0;
            }

            if (delta < 2) {
                // Creates buffer graphics to pass to methods.
                BufferStrategy bs = getBufferStrategy();

                if (bs == null) {
                    createBufferStrategy(3);
                    continue;
                }

                Graphics g = bs.getDrawGraphics();

                Main.graphics = g;

                Input.update();

                if (isConnected && Main.dTime() != 0 && !World.isNull()) {
//                    if (!client.getSocket().isClosed()) {
//                        client.disconnect();
//                        return;
//                    }

                    update();
                    render();
                } else {
                    handleMenu();
                }

                bs.show();
                g.dispose();
            }

            long sleepTime = (lastLoopTime - System.nanoTime() + optimalTime) / 1000000;

            if (sleepTime < 0)
                continue;

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Calls every render - use for game logic and other similar non-graphical devices
    public void update() {
        methods.update();
    }

    // Calls every render - use for drawing graphics and other things similar
    public void render() {
        methods.render();
    }

    public void handleMenu() {
        Login.update();
        Login.render();
    }

    public static BufferedImage getImage(String root) {
        return Main.main.getImageFromRoot(root);
    }

    public BufferedImage getImageFromRoot(Object root) {
        BufferedImage image = null;
        InputStream stream = Main.class.getResourceAsStream("/images/" + root);

        if (stream == null) {
            System.err.println("Unrecognized file: /images/" + root.toString());
            return null;
        }
        try {
            image = ImageIO.read(stream);
        } catch (IOException e) {
            System.err.println("ERROR, IMAGE NOT FOUND: " + root);
        } catch (IllegalArgumentException e) {
            System.err.println("ILLEGAL! " + root);
        }

        return image;
    }

    public static Font getFont(String root, float size, int style) {
        InputStream is = Main.class.getResourceAsStream("/fonts/" + root);

        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);

            return font.deriveFont(style, size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void sendPacket(String message) {
        client.send(message);
    }

}