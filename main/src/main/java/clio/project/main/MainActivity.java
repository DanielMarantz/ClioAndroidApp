package clio.project.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by Daniel Marantz on 17/08/15.
 *
 * MainActivity is the Activity of the App.
 */
public class MainActivity extends Activity {

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState Saved aspects of the app.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Custom title bar
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                R.layout.customtitlebar);
    }
}
