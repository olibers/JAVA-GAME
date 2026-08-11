import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class AudioManager {

    private Clip backgroundClip;
    private Clip floodClip;
    private Clip typhoonClip;
    private Clip dialogueClip;
    private Clip earthquakeClip;

    private String currentMusic = "";

    public AudioManager() {
        backgroundClip = loadSound("/assets/audio/background.wav");
        floodClip = loadSound("/assets/audio/flood.wav");
        typhoonClip = loadSound("/assets/audio/typhoon.wav");
        dialogueClip = loadSound("/assets/audio/dialogue.wav");
        earthquakeClip = loadSound("/assets/audio/earthquake.wav");
    }

    private Clip loadSound(String path) {

        try {

            InputStream audioSrc = getClass().getResourceAsStream(path);

            if (audioSrc == null) {
                System.out.println("Audio not found: " + path);
                return null;
            }

            InputStream bufferedIn =
                    new BufferedInputStream(audioSrc);

            AudioInputStream ais =
                    AudioSystem.getAudioInputStream(bufferedIn);

            Clip clip = AudioSystem.getClip();

            clip.open(ais);

            return clip;

        } catch (Exception e) {

            System.out.println(
                "Could not load audio: " + path
            );

            e.printStackTrace();

            return null;
        }
    }

    // ==========================================
    // PLAY NORMAL BACKGROUND
    // ==========================================

    public void playBackground() {

        if (currentMusic.equals("background")) {
            return;
        }

        stopAllMusic();

        if (backgroundClip != null) {

            backgroundClip.setFramePosition(0);

            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);

            backgroundClip.start();

            currentMusic = "background";
        }
    }

    // ==========================================
    // PLAY FLOOD MUSIC
    // ==========================================

    public void playFlood() {

        if (currentMusic.equals("flood")) {
            return;
        }

        stopAllMusic();

        if (floodClip != null) {

            floodClip.setFramePosition(0);

            floodClip.loop(Clip.LOOP_CONTINUOUSLY);

            floodClip.start();

            currentMusic = "flood";
        }
    }

    // ==========================================
    // PLAY TYPHOON MUSIC
    // ==========================================

    public void playTyphoon() {

        if (currentMusic.equals("typhoon")) {
            return;
        }

        stopAllMusic();

        if (typhoonClip != null) {

            typhoonClip.setFramePosition(0);

            typhoonClip.loop(Clip.LOOP_CONTINUOUSLY);

            typhoonClip.start();

            currentMusic = "typhoon";
        }
    }

    // ==========================================
    // DIALOGUE SOUND
    // ==========================================

    public void playDialogue() {

        if (dialogueClip != null) {

            dialogueClip.stop();

            dialogueClip.setFramePosition(0);

            dialogueClip.loop(Clip.LOOP_CONTINUOUSLY)
            dialogueClip.start();
        }
    }

    public void stopDialogue() {

        if (dialogueClip != null) {

            dialogueClip.stop();

            dialogueClip.setFramePosition(0);
        }
    }

    // ==========================================
    // EARTHQUAKE SOUND
    // ==========================================

    public void playEarthquake() {

        if (earthquakeClip != null) {

            earthquakeClip.stop();

            earthquakeClip.setFramePosition(0);

            earthquakeClip.loop(Clip.LOOP_CONTINUOUSLY);

            earthquakeClip.start();
        }
    }

    public void stopEarthquake() {

        if (earthquakeClip != null) {

            earthquakeClip.stop();

            earthquakeClip.setFramePosition(0);
        }
    }

    // ==========================================
    // STOP ALL MUSIC
    // ==========================================

    public void stopAllMusic() {

        if (backgroundClip != null)
            backgroundClip.stop();

        if (floodClip != null)
            floodClip.stop();

        if (typhoonClip != null)
            typhoonClip.stop();

        currentMusic = "";
    }

    // ==========================================
    // STOP EVERYTHING
    // ==========================================

    public void stopAll() {

        stopAllMusic();

        stopDialogue();

        stopEarthquake();
    }
}