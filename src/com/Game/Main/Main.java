package com.Game.Main;

import com.Game.Player.Player;
import com.Game.Projectile.Projectile;
import com.Game.listener.Input;
import com.Util.Math.Vector2;
import com.Util.Other.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Main extends Canvas {

    private boolean running = false;

    public static double fps = 0;
    public static Main main;
    public static JFrame frame;
    public static Dimension screenSize;

    public Player player;
    public Menu settings;
    public static ArrayList<Projectile> projectiles;

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
        projectiles = new ArrayList<Projectile>();
        player = new Player(new Vector2(100, 100), 150f, Color.GREEN, 2f, 40);
        settings = new Menu();
    }

    public void updateFrame() {

        if (Settings.fullScreen) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            frame.setExtendedState(0);
        }

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
        if (Input.GetKeyDown(KeyEvent.VK_ESCAPE))
            Settings.pause = !Settings.pause;

        if (Input.GetKeyDown(KeyEvent.VK_F1))
            Settings.showFPS = !Settings.showFPS;

        if (Settings.pause)
            return;

        player.update();

    }

    // Calls every tick - use for drawing graphics and other things similar
    public void render(Graphics g) {
        // Wipe screen every tick
        g.setColor(new Color(245, 245, 245));
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());

        if (!Settings.pause) {
            for (int i = 0; i < projectiles.size(); i++)
                projectiles.get(i).projectileUpdate(g, i);

            player.render(g);

            settings.curSelected = 0;
            settings.state = Menu.MenuState.PauseMenu;


        } else {
            // Enter Pause Menu
            settings.render(g);
            settings.update();
        }

        if (Settings.showFPS) {
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.setColor(Color.BLACK);
            g.drawString("FPS: " + (int) Main.fps, 15, 25);
        }


    }
}
