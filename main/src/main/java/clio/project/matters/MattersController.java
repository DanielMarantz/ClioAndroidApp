package clio.project.matters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
     * Converts a JSON object into a Matters object.
     *
     * @param obj            JSON object to be converted.
     * @return               Instantiated Matters object.
     * @throws JSONException
     */
    protected Matters convertMatter(JSONObject obj) throws JSONException {
        // Nested data in JSON object
        JSONObject nestedObj = (JSONObject)obj.get("client");

        String displayName = obj.getString("display_number");
        String clientName = nestedObj.getString("name");
        String description = obj.getString("description");
        String openDate = obj.getString("open_date");
        String status = obj.getString("status");

        return new Matters(displayName, clientName, description, openDate, status);
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
                prefList.add(convertMatter(jsonArray.getJSONObject(i)));
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
    protected void matterDetails(int position, ListAdapter adpt, Context context) {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        dialog.setContentView(R.layout.matter_details);
        dialog.setTitle("DETAILS");
        dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.clio_logo);
        dialog.setCancelable(false);
        // Set the custom dialog components - text, button
        TextView displayText = (TextView) dialog.findViewById(R.id.displayName);
        displayText.setText(adpt.getItem(position).getDisplayName());

        TextView clientText = (TextView) dialog.findViewById(R.id.clientName);
        clientText.setText(adpt.getItem(position).getClientName());

        TextView descText = (TextView) dialog.findViewById(R.id.description);
        descText.setText(adpt.getItem(position).getDescription());

        TextView openDateText = (TextView) dialog.findViewById(R.id.openDate);
        openDateText.setText(adpt.getItem(position).getOpenDate());

        TextView statusText = (TextView) dialog.findViewById(R.id.status);
        statusText.setText(adpt.getItem(position).getStatus());
        // Close dialog button
        Button closeButton = (Button) dialog.findViewById(R.id.closeDialog);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * Displays an alert dialog regarding Network && no persisted data.
     * Click event exits application.
     *
     * @param context The context of a Activity.
     */
    protected void displayAlert(final Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setTitle("No Saved Data Or Network Connection");
        // Set dialog message
        alertDialogBuilder
                .setMessage("Need network connection to retrieve initial data!")
                .setCancelable(false)
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Close current activity
                        ((Activity)(context)).finish();
                    }
                });
        // Create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
