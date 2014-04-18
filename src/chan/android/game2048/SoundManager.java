package chan.android.game2048;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager {

    private int slideId;

    private SoundPool soundPool;

    private AudioManager audioManager;

    private float volume;

    private boolean off = false;

    public SoundManager(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        volume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        slideId = soundPool.load(context, R.raw.sound_slide, 1);
    }

    public void turnOff() {
        off = true;
    }

    public void turnOn() {
        off = false;
    }

    public void playSliding() {
        if (!off) {
            soundPool.play(slideId, volume, volume, 1, 0, 1.0f);
        }
    }
}
