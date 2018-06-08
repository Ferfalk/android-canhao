package com.ferfalk.canhao;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.IOException;

/**
 * Created by Ferahf on 15/10/2015.
 */
public class SoundManager {
    private Context context;
    private SoundPool soundPool;
    private float rate = 1.0f;
    private float masterVolume = 1.0f;
    private float leftVolume = 1.0f;
    private float rightVolume = 1.0f;
    private float balance = 0.5f;

    public SoundManager(Context context) {
        this.context = context;
        soundPool = new SoundPool(16, AudioManager.STREAM_MUSIC, 100);
    }

    // Carrega um sound e retorna o id
    public int load(String arquivo) throws IOException {
        AssetFileDescriptor descriptor = context.getAssets().openFd(arquivo);
        return soundPool.load(descriptor, 0);
    }

    public void play(int sound_id) {
        soundPool.play(sound_id, leftVolume, rightVolume, 1, 0, rate);
    }

    public void setVolume(float vol) {
        masterVolume = vol;

        if (balance < 1.0f) {
            leftVolume = masterVolume;
            rightVolume = masterVolume * balance;
        } else {
            rightVolume = masterVolume;
            leftVolume = masterVolume * (2.0f - balance);
        }

    }

    public void setSpeed(float speed) {
        rate = speed;

        if (rate < 0.01f)
            rate = 0.01f;

        if (rate > 2.0f)
            rate = 2.0f;
    }

    public void setBalance(float balVal) {
        balance = balVal;
        setVolume(masterVolume);
    }

    public void unloadAll() {
        soundPool.release();
    }
}
