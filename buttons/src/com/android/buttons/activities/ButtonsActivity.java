package com.android.buttons.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.UserHandle;
import android.preference.PreferenceActivity;
import android.provider.Settings;

import com.android.buttons.utils.Preferences;
import com.android.buttons.fragments.ButtonsFragment;

public class ButtonsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Preferences.BUTTONS_BOOTED) {
            for (String key : Preferences.KEYS_BUTTONS) {
                if (key.startsWith("buttons_slider")) {
                    Settings.System.putIntForUser(this.getContentResolver(), key, (int) Preferences.DEFAULTS.get(key), UserHandle.USER_CURRENT);
                }

                Preferences.setPreferenceToNode(key, String.valueOf(Preferences.getPreferenceValue(getApplicationContext(), key)));
            }

            Preferences.BUTTONS_BOOTED = true;
        }

        getFragmentManager().beginTransaction().replace(android.R.id.content, new ButtonsFragment()).commit();
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

}
