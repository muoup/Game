package com.Game.Main;

import com.Game.Entity.Player.Player;
import com.Game.GUI.GUI;
import com.Game.Networking.Network;
import com.Game.World.World;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class Main extends Canvas {

    private boolean running = false;

    public static double fps = 0;
    public static Main main;
    public static JFrame frame;
    public static Dimension screenSize;
    public static Graphics graphics;

    public static Player player;
    public static Menu settings;

    public static MethodHandler methods;

    public static void main(String[] args) {
        Main mainObj = new Main();
        JFrame frame = new JFrame("Game");
        Input input = new Input();
        Vector2 res = Settings.curResolution();

        frame.add(mainObj);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize((int) res.x, (int) res.y);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);

        mainObj.running = true;

        Main.frame = frame;
        Main.main = mainObj;

        mainObj.init();
        mainObj.updateFrame();
        mainObj.addKeyListener(input);
        mainObj.addMouseListener(input);
        mainObj.addMouseMotionListener(input);
        mainObj.run();
    }

    public void init(){
        Main.screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        player = new Player(Settings.playerSpawn, 250f, Color.GREEN, 2f);
        settings = new Menu();

        Settings.npcFont = getFont("npc-text.ttf", 20, Font.BOLD);

        methods = new MethodHandler();

        World.changeWorld(0);

        initMethods();
    }

    public void initMethods() {
        methods.player = player;
        methods.settings = settings;

        GUI.init();
        Network.init();
    }

    // Used for the shift of the settings
    public void updateFrame() {

        if (Settings.fullScreen) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            frame.setExtendedState(0);
        }

        frame.setSize((int) Settings.curResolution().x, (int) Settings.curResolution().y);
        frame.setLocationRelativeTo(null);

        Settings.itemFont = new Font("Arial", Font.PLAIN, (int) Settings.curResolution().x / 75);

        GUI.init();
    }

    // Calculate fps and run other functions
    public void run() {

        // NOTE: just don't touch any of this, IntBoxSize got this off of StackOverFlow
        double lastFpsTime = 0;
        double lfps = 0;
        long lastLoopTime = System.nanoTime();

        while (running) {
            long optimalTime = 1000000000 / Settings.fpsCap;
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;

            lastLoopTime = now;

            double delta = updateLength / ((double)optimalTime);

            lastFpsTime += updateLength;
            lfps++;

            if (lastFpsTime >= 1000000000) {
                lastFpsTime = 0;
                Main.fps = lfps;
                lfps = 0;
            }

            // Creates buffer graphics to pass to methods.
            BufferStrategy bs = getBufferStrategy();

            if (bs == null) {
                createBufferStrategy(3);
                continue;
            }

            Graphics g = bs.getDrawGraphics();

            Main.graphics = g;


            update();
            render();

            bs.show();
            g.dispose();

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

    // Calls every tick - use for game logic and other similar non-graphical devices
    public void update() {
        methods.update();
    }

    // Calls every tick - use for drawing graphics and other things similar
    public void render() {
        methods.render();
    }

    public static BufferedImage getImage(String root) {
        return Main.main.getImageFromRoot(root);
    }

    public BufferedImage getImageFromRoot(String root) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResource("/res/images/" + root));
        } catch (IOException e) {
            System.err.println("ERROR, IMAGE NOT FOUND: " + root);
        }

        return image;
    }

    public Font getFont(String root, float size, int style) {
        InputStream is = Main.class.getResourceAsStream("/res/fonts/" + root);

        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);

            return font.deriveFont(style, size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}