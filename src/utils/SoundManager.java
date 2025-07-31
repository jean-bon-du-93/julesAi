package utils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

/**
 * Manages loading and playing of sound effects.
 */
public class SoundManager {
    public static Clip eatSound;
    public static Clip dieSound;

    static {
        try {
            eatSound = loadClip("res/eat.wav");
            dieSound = loadClip("res/die.wav");
        } catch (Exception e) {
            System.err.println("Could not load sounds. Make sure they are in the 'res' directory.");
            e.printStackTrace();
        }
    }

    private static Clip loadClip(String filename) throws Exception {
        File soundFile = new File(filename);
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        return clip;
    }

    public static void play(Clip clip) {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }
}
