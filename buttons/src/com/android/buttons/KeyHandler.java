package com.android.buttons;

import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.KeyEvent;

import com.android.internal.os.DeviceKeyHandler;

import com.android.buttons.utils.Keycodes;

import java.util.Arrays;

public class KeyHandler implements DeviceKeyHandler {

    private Context mContext;
    private AudioManager mAudioManager;
    private Vibrator mVibrator;

    public KeyHandler(Context context) {
        mContext = context;

        mAudioManager = mContext.getSystemService(AudioManager.class);
        mVibrator = mContext.getSystemService(Vibrator.class);
    }

    public KeyEvent handleKeyEvent(KeyEvent event) {
        int scanCode = event.getScanCode();

        switch (scanCode) {
            case Keycodes.KEYCODE_SLIDER_RING:
                mAudioManager.setRingerModeInternal(AudioManager.RINGER_MODE_NORMAL);
                break;
            case Keycodes.KEYCODE_SLIDER_VIBRATE:
                mAudioManager.setRingerModeInternal(AudioManager.RINGER_MODE_VIBRATE);
                break;
            case Keycodes.KEYCODE_SLIDER_SILENT:
                mAudioManager.setRingerModeInternal(AudioManager.RINGER_MODE_SILENT);
                break;
            default:
                return event;
        }
        doHapticFeedback();

        return null;
    }

    private void doHapticFeedback() {
        if (mVibrator == null) {
            return;
        }

        mVibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
    }
}
