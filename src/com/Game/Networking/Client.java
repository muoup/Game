package com.Game.Networking;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.GUI.Skills.Skills;
import com.Game.Main.Main;
import com.Game.Main.Menu;
import com.Game.Main.MethodHandler;
import com.Util.Math.Vector2;
import com.Util.Other.Settings;

import javax.swing.*;
import java.io.IOException;
import java.net.*;

public class Client {

    public enum Error {
        NONE, INVALID_HOST, SOCKET_EXCEPTION
    }

    private String ipAddress;
    private int port;
    private boolean listening = false;
    private byte[] dataBuffer = new byte[4096];

    private Error errorCode = Error.NONE;

    private InetAddress serverAddress;

    private DatagramSocket socket;

    private Thread listenerThread;

    public Client(String host) {
        String[] parts = host.split(":");

        if (parts.length != 2) {
            errorCode = Error.INVALID_HOST;
            return;
        }

        this.ipAddress = parts[0];

        try {
            this.port = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            errorCode = Error.INVALID_HOST;
            return;
        }
    }

    public Client(String host, int port) {
        this.ipAddress = host;
        this.port = port;
    }

    public void listen() {
        while (listening) {
            DatagramPacket packet = new DatagramPacket(dataBuffer, dataBuffer.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            process(packet);
        }
    }

    private void process(DatagramPacket packet) {
        byte[] data = packet.getData();
        String content = new String(data);
//        System.out.println("PACKET RECEIVED: " + content);
        String start = content.substring(0, 2);
        String message = content.substring(2).trim();
        String[] index;

        switch (start) {
            case "01":
                Main.player.serverIndex = Integer.parseInt(message);
                break;
            case "02":
                index = message.split(":");
                boolean c = message.charAt(1) == 'c';
                if (message.charAt(0) == 'l') {
                    if (c) {
                        joinServer(index[1]);
                    } else {
                        JOptionPane.showMessageDialog(null, "The username and password is not recognized.");
                    }
                } else if (message.charAt(0) == 'r') {
                    if (c) {
                        joinServer(index[1]);
                    } else {
                        JOptionPane.showMessageDialog(null, "That username already exists!");
                    }
                } else if (message.charAt(0) == 'p') {
                    JOptionPane.showMessageDialog(null, "That player is already logged in. Wait a second and retry again.");
                } else {
                    System.err.println("An unexpected login message occured.");
                }
                break;
            case "13":
            case "55":
                // Message is received
                ChatBox.sendMessage(message);
                break;
            case "12":
                String[] parts = message.split(":");
                MethodHandler.playerConnections.add(new PlayerObject(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), parts[2]));
                break;
            case "15":
                String[] movement = message.split(":");
                for (PlayerObject o : MethodHandler.playerConnections) {
                    if (o.getUsername().equals(movement[0].trim())) {
                        o.setPos(Integer.parseInt(movement[1]), Integer.parseInt(movement[2]));
                        break;
                    }
                }
                break;
            case "76":
                send("76" + Main.player.name);
                break;
            case "66":
                for (PlayerObject o : MethodHandler.playerConnections) {
                    if (o.getUsername().equals(message)) {
                        MethodHandler.playerConnections.remove(o);
                        break;
                    }
                }
                break;
            case "99":
                Main.logout();
                break;
            case "04":
                takeSkillData(message.split(":"));
                break;
            case "05":
                takeItemData(message.split(":"));
                break;
            case "06":
                takeAccData(message.split(":"));
                break;
        }

        dataBuffer = new byte[4096];
    }

    public boolean connect(String username, String password, int connectionCode) {
        try {
            serverAddress = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            errorCode = Error.INVALID_HOST;
            return false;
        }

        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
            errorCode = Error.SOCKET_EXCEPTION;
            return false;
        }
        Main.player.name = username;
        send(Main.connectionCode + username + ":" + password + ":" + connectionCode + ":" + (int) Main.player.position.x + ":" + (int) Main.player.position.y);
        // Wait for server to reply
        listening = true;

        listenerThread = new Thread(() -> listen());
        listenerThread.start();
        return true;
    }

    public void takeSkillData(String[] index) {
        // 0 - Username; 1 & 2 -> pos; 3-? -> skills;
        Main.player.name = index[0];
        Main.player.position = new Vector2(Integer.parseInt(index[1]), Integer.parseInt(index[2]));
        for (int i = 3; i < index.length; i++) {
            Skills.setExperience(i - 3, Float.parseFloat(index[i]));
        }
    }

    public void takeItemData(String[] index) {
        int id = 0;
        for (int i = 1; i < index.length; i++) {
            if (i % 2 == 1) {
                id = Integer.parseInt(index[i]);
            } else {
                InventoryManager.clientSetItem(i / 2 - 1, id, Integer.parseInt(index[i]));
            }
        }
    }

    public void takeAccData(String[] index) {
        int id = 0;
        for (int i = 1; i < index.length; i++) {
            if (i % 2 == 1) {
                id = Integer.parseInt(index[i]);
            } else {
                AccessoriesManager.clientSetItem(i / 2 - 1, id, Integer.parseInt(index[i]));
            }
        }
    }

    public void joinServer(String username) {

        System.out.println("CLIENT NAME: " + username);

        Main.player.name = username;
        ChatBox.tag = "[" + Main.player.name + "]: ";

        //sendConnectionPacket(username, password);
        System.out.println("POS: " + Main.player.position);

        Main.isConnected = true;
    }

    public void disconnect() {
        send("55" + Main.player.name);
        MethodHandler.playerConnections.clear();
        Settings.pause = false;
        Main.settings.state = Menu.MenuState.SimplePause;
        InventoryManager.reset();
        AccessoriesManager.init();
        Main.logout();
    }

    public void send(byte[] data) {
        assert(socket.isConnected());
        DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message) {
        send(message.getBytes());
    }

    public Error getErrorCode() {
        return errorCode;
    }
}
