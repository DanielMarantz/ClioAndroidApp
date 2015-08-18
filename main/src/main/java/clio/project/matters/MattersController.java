package clio.project.matters;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import clio.project.main.DialogLoader;
import clio.project.main.ListAdapter;
import clio.project.main.Network;
import clio.project.main.R;
import clio.project.main.SharedPreference;

/**
 * Created by Daniel Marantz on 16/08/15.
 *
 * MattersController is the controller for the Matters objects.
 */
public class MattersController {

    private SharedPreference mattersDatabase = new SharedPreference();
    private Network network = new Network();
    private MattersParser matterParser = new MattersParser();
    private DialogLoader dialogLoader = new DialogLoader();

    /**
     * Converts a stream of data to a string. Also persists string data.
     *
     * @param inputStream  Stream of data to be converted to a String.
     * @param context      The context of a Activity.
     * @return             String of data that has been converted.
     * @throws IOException
     */
    protected String convertInputStreamToString(InputStream inputStream, Context context) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while((line = bufferedReader.readLine()) != null) {
            result += line;
        }
        // Save JSON String in Shared Preferences
        // Saving it in this method allows the data to be up to date
        mattersDatabase.save(result, context);
        inputStream.close();

        return result;
    }

    /**
     * Handles JSON to Matters object parsing.
     *
     * @param obj            JSON object to be converted.
     * @return               Instantiated Matters object.
     * @throws JSONException
     */
    protected Matters parseMatter(JSONObject obj) throws JSONException {
        return matterParser.parse(obj);
    }

    /**
     * Given a String in JSON format converted to a list of Matters.
     *
     * @param matterData JSON data.
     * @return           List of Matters.
     */
    protected ArrayList<Matters> populateList(String matterData)  {
        ArrayList<Matters> prefList = new ArrayList<Matters>();

        try {
            JSONObject jsnObject = new JSONObject(matterData);
            JSONArray jsonArray = jsnObject.getJSONArray("matters");

            for (int i=0; i < jsonArray.length(); i++) {
                prefList.add(parseMatter(jsonArray.getJSONObject(i)));
            }
            return prefList;
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    /**
     * Checks if data is persisted, retrieves data.
     *
     * @param context The context of a Activity.
     * @return        The restored data.
     */
    protected String restoreMatter(Context context) {
        if(mattersDatabase.isData(context)) {
            return mattersDatabase.getValue(context);
        }
        return null;
    }

    /**
     * Checks the device for network connectivity
     *
     * @param context The context of a Activity.
     * @return        State of internet connection
     */
    protected boolean checkNetwork(Context context) {
        if(network.isInternet(context)) {
            return true;
        }
        return false;
    }

    /**
     * Handles the server response.
     *
     * @param params The url to send and receive.
     * @return       Response of the request.
     */
    protected InputStream serverResponse(String... params) {
        return network.connect(params);
    }

    /**
     * Setter of the total matters in custom title bar.
     *
     * @param total   The total of Matters.
     * @param context The context of a Activity.
     */
    protected void setTotalMatters(int total, Context context) {
        TextView totalMatters = (TextView)((Activity)(context)).findViewById(R.id.totalNumber);
        totalMatters.setText("" + total);
    }

    /**
     * Displays Matter details in a custom dialog.
     *
     * @param position The location of the Matter in the list.
     * @param adpt     The custom list adapter reference.
     * @param context  The context of a Activity.
     */
    protected void displayDetails(int position, ListAdapter adpt, Context context) {
        dialogLoader.loadDetails(position, adpt, context);
    }

    /**
     * Displays an alert dialog.
     *
     * @param context The context of a Activity.
     */
    protected void displayAlert(final Context context) {
        dialogLoader.loadAlert(context);
    }

    /**
     * Locks the screen orientation given its current orientation.
     *
     * @param context The context of a Activity.
     */
    public void lockScreenOrientation(Context context) {
        int currentOrientation = context.getResources().getConfiguration().orientation;

        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            ((Activity)(context)).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            ((Activity)(context)).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    /**
     * Unlocks the screens orientation.
     *
     * @param context The context of a Activity..
     */
    public void unlockScreenOrientation(Context context) {
        ((Activity)(context)).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }
}
