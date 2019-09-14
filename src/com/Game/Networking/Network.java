package com.Game.Networking;

import com.Game.GUI.Chatbox.ChatBox;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Network implements Runnable {
    private static Socket socket;
    private static ObjectOutputStream output;
    private static ObjectInputStream input;
    private static String serverIP = "127.0.0.1";

    public static void init() {
        Network network = new Network();
        Thread thread = new Thread(network);
        thread.start();
    }

    @Override
    public void run() {
        try {
            setupStreams();
            detectPackets();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Network.closeConnection();
        }
    }

    public static void setupStreams() throws IOException {
        socket = new Socket(InetAddress.getByName(serverIP), 3000);

        output = new ObjectOutputStream(socket.getOutputStream());
        output.flush();

        input = new ObjectInputStream(socket.getInputStream());
    }

    public static synchronized void detectPackets() throws IOException {
        while(true) {
            try {
                PacketObject message = (PacketObject) input.readObject();
                ChatBox.sendMessage(message.getMessage());
            } catch (ClassNotFoundException e) {
                System.err.println("This user has sent an unknown object!");
            }
        }
    }

    public static void sendPackets(final String packet) {
        try {
            output.writeObject(packet);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        try {
            output.close();
            input.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
