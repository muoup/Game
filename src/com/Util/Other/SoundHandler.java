package com.Util.Other;

import com.Game.Main.Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.InputStream;

public class SoundHandler {
    public static void playSound(final String fileName) {
        // So the sound plays asynchronously, create a new thread.
        new Thread(() -> {
            try {
                // Open an audio input stream.
                InputStream stream = Main.class.getResourceAsStream("/res/sfx/" + fileName);
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(stream);
                // Get a sound clip resource.
                Clip clip = AudioSystem.getClip();
                // Open audio clip and load samples from the audio input stream.
                clip.open(audioIn);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).run();
    }
}
