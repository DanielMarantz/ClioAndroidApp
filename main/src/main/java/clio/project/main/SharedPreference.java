package clio.project.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Daniel Marantz on 14/08/15.
 *
 * SharedPreference is the persistence controller of the app.
 */
public class SharedPreference {

    private static final String PREFS_NAME = "MATTERS_FILE";
    private static final String PREFS_KEY = "MATTER_DATA";

    /**
     * Saves String data to a file given a key.
     *
     * @param text    String of data to be persisted.
     * @param context The context of the activity.
     */
    public void save(String text,  Context context) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(PREFS_KEY, text);
        editor.commit();
    }

    /**
     * Retrieves String data from a file given a key.
     *
     * @param context The context of the activity.
     * @return        The data retrieved as a String.
     */
    public String getValue(Context context) {
        SharedPreferences settings;
        String restoredText;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        restoredText = settings.getString(PREFS_KEY, null);

        return restoredText;
    }

    /**
     * Removes all data for a file.
     *
     * @param context The context of the activity.
     */
    public void clearSharedPreference(Context context) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * Removes data from a file given a key.
     *
     * @param context The context of the activity.
     */
    public void removeValue(Context context) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.remove(PREFS_KEY);
        editor.commit();
    }

    /**
     * Checks if data is persisted in a file given a key.
     *
     * @param context The context of the activity.
     * @return        A Boolean containing state of stored data.
     */
    public boolean isData(Context context) {
        SharedPreferences settings;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (settings.contains(PREFS_KEY)) {
            return true;
        }
        return false;
    }
}
