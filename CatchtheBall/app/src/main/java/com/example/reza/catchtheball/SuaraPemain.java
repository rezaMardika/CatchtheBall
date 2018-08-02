package com.example.reza.catchtheball;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

/**
 * Created by reza on 08/07/18.
 */

public class SuaraPemain {

    private AudioAttributes audioAttributes;
    final int Sound_Pool_Max = 2;

    private static SoundPool soundPool;
    private static int hitSound;
    private static int overSound;

    public SuaraPemain(Context context){

        //karena soundPool tidak dipergunakan pada API level 21. (Lollipop)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            audioAttributes = new AudioAttributes.Builder().
                    setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(Sound_Pool_Max)
                    .build();
        }else {
            //SoundPool (int maxStream, int streamType, int srcQuality)
            soundPool = new SoundPool(Sound_Pool_Max, AudioManager.STREAM_MUSIC, 0);

        }

        hitSound = soundPool.load(context, R.raw.hit, 1);
        overSound = soundPool.load(context, R.raw.over, 1);
    }

    public void playHitSound(){
        soundPool.play(hitSound, 1.0f, 1.0f, 1,0, 1.0f);
    }

    public void playOverSound(){
        soundPool.play(overSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}
