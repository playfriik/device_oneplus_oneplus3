package com.android.buttons.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.SeekBarPreference;
import androidx.preference.SwitchPreference;

import com.android.buttons.R;
import com.android.buttons.utils.Preferences;

public class ButtonsFragment extends PreferenceFragment {

    private Handler mHandler;

    private SwitchPreference mVirtual;
    private SeekBarPreference mHardwareBacklightBrightness;
    private SwitchPreference mHardwareSwap;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_buttons, rootKey);

        mHandler = new Handler();

        mVirtual = findPreference("buttons_virtual");
        mVirtual.setChecked((int) Preferences.getPreferenceValue(getActivity().getApplicationContext(), Preferences.KEY_BUTTONS_VIRTUAL) == 1);
        mVirtual.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object objectValue) {
                int value = (boolean) objectValue ? 1 : 0;

                Settings.System.putIntForUser(getActivity().getContentResolver(), Preferences.KEY_BUTTONS_VIRTUAL, value, UserHandle.USER_CURRENT);
                Settings.System.putIntForUser(getActivity().getContentResolver(), Settings.System.NAVIGATION_BAR_SHOW, value, UserHandle.USER_CURRENT);

                mVirtual.setEnabled(false);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mVirtual.setEnabled(true);
                    }
                }, 1500);

                Preferences.setPreferenceToNode(Preferences.KEY_BUTTONS_HARDWARE_BACKLIGHT_BRIGHTNESS, String.valueOf(Preferences.getPreferenceValue(getActivity().getApplicationContext(), Preferences.KEY_BUTTONS_HARDWARE_BACKLIGHT_BRIGHTNESS)));
                Preferences.setPreferenceToNode(Preferences.KEY_BUTTONS_HARDWARE_SWAP, String.valueOf(Preferences.getPreferenceValue(getActivity().getApplicationContext(), Preferences.KEY_BUTTONS_HARDWARE_SWAP)));
                return true;
            }
        });

        mHardwareBacklightBrightness = findPreference("buttons_hardware_backlight_brightness");
        mHardwareBacklightBrightness.setValue((int) Preferences.getPreferenceValue(getActivity().getApplicationContext(), Preferences.KEY_BUTTONS_HARDWARE_BACKLIGHT_BRIGHTNESS));
        mHardwareBacklightBrightness.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object objectValue) {
                int value = (int) objectValue;

                Settings.System.putIntForUser(getActivity().getContentResolver(), Preferences.KEY_BUTTONS_HARDWARE_BACKLIGHT_BRIGHTNESS, value, UserHandle.USER_CURRENT);

                Preferences.setPreferenceToNode(Preferences.KEY_BUTTONS_HARDWARE_BACKLIGHT_BRIGHTNESS, String.valueOf(value));
                return true;
            }
        });

        mHardwareSwap = findPreference("buttons_hardware_swap");
        mHardwareSwap.setChecked((int) Preferences.getPreferenceValue(getActivity().getApplicationContext(), Preferences.KEY_BUTTONS_HARDWARE_SWAP) == 1);
        mHardwareSwap.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object objectValue) {
                int value = (boolean) objectValue ? 1 : 0;

                Settings.System.putIntForUser(getActivity().getContentResolver(), Preferences.KEY_BUTTONS_HARDWARE_SWAP, value, UserHandle.USER_CURRENT);

                Preferences.setPreferenceToNode(Preferences.KEY_BUTTONS_HARDWARE_SWAP, String.valueOf(value));
                return true;
            }
        });
    }

}
