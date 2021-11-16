package com.android.alertslider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.provider.Settings;

import com.android.alertslider.utils.Preferences;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Preferences.BOOT_COMPLETED) {
            for (String key : Preferences.KEYS_BUTTONS) {
                if (key.startsWith("buttons_slider")) {
                    Settings.System.putIntForUser(context.getContentResolver(), key, (int) Preferences.DEFAULTS.get(key), UserHandle.USER_CURRENT);
                }

                Preferences.setPreferenceToNode(key, String.valueOf(Preferences.getPreferenceValue(context, key)));
            }

            Preferences.BOOT_COMPLETED = true;
        }
    }

}
