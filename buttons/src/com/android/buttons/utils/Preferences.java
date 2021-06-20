package com.android.buttons.utils;

import android.content.Context;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Preferences {

    private static String TAG = "Buttons-Preferences";

    public static boolean BUTTONS_BOOTED = false;

    public static final String KEY_BUTTONS_VIRTUAL = "buttons_virtual";
    public static final String KEY_BUTTONS_HARDWARE_BACKLIGHT_BRIGHTNESS = "buttons_hardware_backlight_brightness";
    public static final String KEY_BUTTONS_HARDWARE_SWAP = "buttons_hardware_swap";

    public static final String KEY_BUTTONS_SLIDER_TOP = "buttons_slider_top";
    public static final String KEY_BUTTONS_SLIDER_MIDDLE = "buttons_slider_middle";
    public static final String KEY_BUTTONS_SLIDER_BOTTOM = "buttons_slider_bottom";

    public static final String[] KEYS_BUTTONS = {KEY_BUTTONS_VIRTUAL, KEY_BUTTONS_HARDWARE_BACKLIGHT_BRIGHTNESS, KEY_BUTTONS_HARDWARE_SWAP, KEY_BUTTONS_SLIDER_TOP, KEY_BUTTONS_SLIDER_MIDDLE, KEY_BUTTONS_SLIDER_BOTTOM};

    public static final String[] NODES_BUTTONS_HARDWARE_BACKLIGHT_BRIGHTNESS = {"/sys/devices/soc/leds-qpnp-18/leds/button-backlight/brightness", "/sys/devices/soc/leds-qpnp-18/leds/button-backlight/max_brightness"};
    public static final String[] NODES_BUTTONS_HARDWARE_SWAP = {"/proc/s1302/key_rep"};
    public static final String[] NODES_BUTTONS_SLIDER_TOP = {"/proc/tri-state-key/keyCode_top"};
    public static final String[] NODES_BUTTONS_SLIDER_MIDDLE = {"/proc/tri-state-key/keyCode_middle"};
    public static final String[] NODES_BUTTONS_SLIDER_BOTTOM = {"/proc/tri-state-key/keyCode_bottom"};

    public static HashMap<String, Object> DEFAULTS = new HashMap<>();
    static {
        DEFAULTS.put(KEY_BUTTONS_VIRTUAL, 0);
        DEFAULTS.put(KEY_BUTTONS_HARDWARE_BACKLIGHT_BRIGHTNESS, 40);
        DEFAULTS.put(KEY_BUTTONS_HARDWARE_SWAP, 0);

        DEFAULTS.put(KEY_BUTTONS_SLIDER_TOP, Keycodes.KEYCODE_SLIDER_SILENT);
        DEFAULTS.put(KEY_BUTTONS_SLIDER_MIDDLE, Keycodes.KEYCODE_SLIDER_VIBRATE);
        DEFAULTS.put(KEY_BUTTONS_SLIDER_BOTTOM, Keycodes.KEYCODE_SLIDER_RING);
    }

    public static HashMap<String, String[]> NODES = new HashMap<>();
    static {
        NODES.put(KEY_BUTTONS_HARDWARE_BACKLIGHT_BRIGHTNESS, NODES_BUTTONS_HARDWARE_BACKLIGHT_BRIGHTNESS);
        NODES.put(KEY_BUTTONS_HARDWARE_SWAP, NODES_BUTTONS_HARDWARE_SWAP);

        NODES.put(KEY_BUTTONS_SLIDER_TOP, NODES_BUTTONS_SLIDER_TOP);
        NODES.put(KEY_BUTTONS_SLIDER_MIDDLE, NODES_BUTTONS_SLIDER_MIDDLE);
        NODES.put(KEY_BUTTONS_SLIDER_BOTTOM, NODES_BUTTONS_SLIDER_BOTTOM);
    }

    public static Object getPreferenceValue(Context context, String key) {
        if (DEFAULTS.get(key) instanceof Integer) {
            int value = Settings.System.getIntForUser(context.getContentResolver(), key, (int) DEFAULTS.get(key), UserHandle.USER_CURRENT);

            return value;
        } else if (DEFAULTS.get(key) instanceof String) {
            String value = Settings.System.getStringForUser(context.getContentResolver(), key, UserHandle.USER_CURRENT);
            if (value == null) {
                value = (String) DEFAULTS.get(key);
            }

            return value;
        }

        return null;
    }

    public static void setPreferenceToNode(String key, String value) {
        if (!NODES.containsKey(key)) {
            return;
        }

        for (String node : NODES.get(key)) {
            if (!writeLine(node, value)) {
                Log.w(TAG, "Write " + value + " to node " + node + " failed");
            }
        }
    }

    public static boolean writeLine(String node, String value) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(node))) {
            bufferedWriter.write(value);
        } catch (FileNotFoundException ignored) {
            Log.w(TAG, "Could not find node: " + node);

            return false;
        } catch (IOException ignored) {
            Log.e(TAG, "Could not write to node: " + node);

            return false;
        }

        return true;
    }

}
