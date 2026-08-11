import javax.sound.sampled.*;

public class AudioManager {

    private void setVolume(Clip clip, float volume) {

    if (clip == null) return;

    try {

        FloatControl gainControl =
            (FloatControl) clip.getControl(
                FloatControl.Type.MASTER_GAIN
            );

        gainControl.setValue(volume);

    } catch (Exception e) {

        System.out.println("Volume control not supported.");
    }
    }


    private Clip backgroundClip;
    private Clip earthquakeClip;
    private Clip typhoonClip;
    private Clip dialogueClip;
    private Clip floodClip;
    

    public AudioManager() {
        backgroundClip = loadSound("/sound/background1.wav");
        earthquakeClip = loadSound("/sound/earthquake.wav");
        typhoonClip = loadSound("/sound/typhoon.wav");
        dialogueClip = loadSound("/sound/dialogue.wav");
        floodClip = loadSound("/sound/flood.wav");
    }

    private Clip loadSound(String audioPath) {
    try {
        java.net.URL soundURL = getClass().getResource(audioPath);
        if (soundURL != null) {
            javax.sound.sampled.AudioInputStream ais = javax.sound.sampled.AudioSystem.getAudioInputStream(soundURL);
            Clip clip = javax.sound.sampled.AudioSystem.getClip();
            clip.open(ais);
            return clip;
        } else {
            System.out.println("Sound not found: " + audioPath);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
    }   
    // =========================================
    // BACKGROUND MUSIC
    // =========================================

    public void playBackground() {

    if (backgroundClip == null) return;

    setVolume(backgroundClip, 0.20f);

    if (!backgroundClip.isRunning()) {

        backgroundClip.setFramePosition(0);

        backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);

        backgroundClip.start();
    }
}

    public void stopBackground() {

        if (backgroundClip != null) {
            backgroundClip.stop();
        }
    }

    // =========================================
    // TYPHOON
    // =========================================

    public void playTyphoon() {

    stopAllEffects();

    if (typhoonClip != null) {

        setVolume(typhoonClip, -10.0f);

        typhoonClip.setFramePosition(0);

        typhoonClip.loop(
            Clip.LOOP_CONTINUOUSLY
        );

        typhoonClip.start();
    }
}

    // =========================================
    // FLOOD
    // =========================================

    public void playFlood() {

    stopAllEffects();

    if (floodClip != null) {

        setVolume(floodClip, -10.0f);

        floodClip.setFramePosition(0);

        floodClip.loop(
            Clip.LOOP_CONTINUOUSLY
        );

        floodClip.start();
    }
}

    // =========================================
    // EARTHQUAKE
    // =========================================

    public void playEarthquake() {

    stopAllEffects();

    if (earthquakeClip != null) {

        setVolume(earthquakeClip, -10.0f);

        earthquakeClip.setFramePosition(0);

        earthquakeClip.start();
    }
}

    // =========================================
    // DIALOGUE
    // =========================================

    public void playDialogue() {

    if (dialogueClip != null) {

        setVolume(dialogueClip, -10.0f);

        dialogueClip.setFramePosition(0);

        dialogueClip.start();
    }
}

    // =========================================
    // STOP EFFECTS
    // =========================================

    public void stopAllEffects() {

        if (typhoonClip != null) {
            typhoonClip.stop();
        }

        if (floodClip != null) {
            floodClip.stop();
        }

        if (earthquakeClip != null) {
            earthquakeClip.stop();
        }

        if (dialogueClip != null) {
            dialogueClip.stop();
        }
    }



    // =================================================
// VOLUME CONTROL
// =================================================

public void setBackgroundVolume(float volume) {

    setVolume(backgroundClip, volume);
}

public void setEffectsVolume(float volume) {

    setVolume(typhoonClip, volume);
    setVolume(floodClip, volume);
    setVolume(earthquakeClip, volume);
    setVolume(dialogueClip, volume);
}


}