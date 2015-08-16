package clio.project.main;

/**
 * Created by Daniel Marantz on 14/08/15.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SharedPreference {

    public static final String PREFS_NAME = "MATTERS_FILE";
    public static final String PREFS_KEY = "MATTER_DATA";

    public SharedPreference() {
        super();
    }

    public void save(Context context, String text) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(PREFS_KEY, text);

        editor.commit();
    }

    public String getValue(Context context) {
        SharedPreferences settings;
        String restoredText;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        restoredText = settings.getString(PREFS_KEY, null);
        return restoredText;
    }

    public void clearSharedPreference(Context context) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.commit();
    }

    public void removeValue(Context context) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.remove(PREFS_KEY);
        editor.commit();
    }

    public boolean isData(Context context) {
        SharedPreferences settings;
        Log.d("restore3", "IsData");
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (settings.contains(PREFS_KEY))
            return true;

        return false;
    }
}
