package com.Game.Main;

import com.Game.Entity.Player.Player;
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

public class Main extends Canvas {

    private boolean running = false;

    public static double fps = 0;
    public static Main main;
    public static JFrame frame;
    public static Dimension screenSize;

    public BufferedImage playerImage;
    public Player player;
    public Menu settings;

    public static MethodHandler methods;

    public BufferedImage worldImage;

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

        try {
            worldImage = ImageIO.read(getClass().getResource("/res/images/worldTest.png"));
            playerImage = ImageIO.read(getClass().getResource("/res/images/player.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        player = new Player(Settings.curResolution().scale(0.5f), 250f, Color.GREEN, 2f, playerImage);
        settings = new Menu();

        methods = new MethodHandler();

        initMethods();
    }

    public void initMethods() {
        methods.player = player;
        methods.settings = settings;
    }
    public void updateFrame() {

        if (Settings.fullScreen) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            frame.setExtendedState(0);
        }

        frame.setSize((int) Settings.curResolution().x, (int) Settings.curResolution().y);

    }

    public void run() {
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

            BufferStrategy bs = getBufferStrategy();

            if (bs == null) {
                createBufferStrategy(3);
                continue;
            }

            Graphics g = bs.getDrawGraphics();

            update();
            render(g);

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
    public void render(Graphics g) {
        BufferedImage subImage = worldImage.getSubimage((int) World.curWorld.offset.x, (int)World.curWorld.offset.y,
                (int)Settings.curResolution().x, (int)Settings.curResolution().y);

        g.drawImage(subImage, 0, 0, (int) Settings.curResolution().x, (int)Settings.curResolution().y, null);

        methods.render(g);
    }
}