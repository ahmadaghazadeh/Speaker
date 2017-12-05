package ahmad.aghazadeh.speaker;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ferdos.s on 5/30/2017.
 */

public class PreferenceManager {

    private final String DEFAULT_PREFERENCE_POSTFIX = "_preferences";

    private final Context context;
    private final String name;
    private final int mode;

    public PreferenceManager(Context context) {
        this.context = context;
        this.name = context.getPackageName() + DEFAULT_PREFERENCE_POSTFIX;
        this.mode = Context.MODE_PRIVATE;
    }

    public PreferenceManager(Context context, int mode) {
        this.context = context;
        this.name = context.getPackageName() + DEFAULT_PREFERENCE_POSTFIX;
        this.mode = Context.MODE_PRIVATE;

    }

    public PreferenceManager(Context context, String name, int mode) {
        this.context = context;
        this.name = name;
        this.mode = Context.MODE_PRIVATE;
    }

    public boolean get(String key, boolean defValue) {
        return getSharedPreferences().getBoolean(key, defValue);
    }

    public float get(String key, float defValue) {
        return getSharedPreferences().getFloat(key, defValue);
    }

    public int get(String key, int defValue) {
        return getSharedPreferences().getInt(key, defValue);
    }

    public long get(String key, long defValue) {
        return getSharedPreferences().getLong(key, defValue);
    }

    public String get(String key, String defValue) {
        return getSharedPreferences().getString(key, defValue);
    }

    public SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(name, mode);
    }

    public void set(String key, boolean value) {
        SharedPreferences prefs = context.getSharedPreferences(name, mode);

        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putBoolean(key, value);
        prefEditor.apply();
    }

    public void set(String key, float value) {
        SharedPreferences prefs = context.getSharedPreferences(name, mode);

        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putFloat(key, value);
        prefEditor.apply();
    }

    public void set(String key, int value) {
        SharedPreferences prefs = context.getSharedPreferences(name, mode);

        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(key, value);
        prefEditor.apply();
    }

    public void set(String key, long value) {
        SharedPreferences prefs = context.getSharedPreferences(name, mode);

        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putLong(key, value);
        prefEditor.apply();
    }

    public void set(String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences(name, mode);

        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(key, value);
        prefEditor.apply();
    }
}
