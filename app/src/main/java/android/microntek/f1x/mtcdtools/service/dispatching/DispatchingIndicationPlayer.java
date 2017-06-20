package android.microntek.f1x.mtcdtools.service.dispatching;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import android.microntek.f1x.mtcdtools.R;

/**
 * Created by COMPUTER on 2017-05-10.
 */

public class DispatchingIndicationPlayer {
    public DispatchingIndicationPlayer(Context context) {
        mMediaPlayer = MediaPlayer.create(context, R.raw.indicator);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public void play() {
        mMediaPlayer.start();
    }

    public void release() {
        mMediaPlayer.release();
    }

    private final MediaPlayer mMediaPlayer;
}
