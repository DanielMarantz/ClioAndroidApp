package clio.project.matters;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import clio.project.main.ListAdapter;
import clio.project.main.R;

/**
 * Created by Daniel Marantz on 15/08/15.
 *
 * MattersFragment is a listview component with Matters.
 * AsyncListViewLoader implemented as a inner anonymous class.
 */
public class MattersFragment extends Fragment {

    private Context context;
    private static final String URL = "https://app.goclio.com/api/v2/matters";

    private Vibrator v;
    private ListAdapter adpt;
    private ListView listView;

    private ArrayList<Matters> result = new ArrayList<Matters>();
    private MattersController mController = new MattersController();

    /**
     * Called when the fragment is first created.
     * Only called once for this fragment.
     *
     * @param savedInstanceState Saved aspects of the app.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Getting application context
        context = getActivity();
        // Instantiating components
        v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        adpt  = new ListAdapter(result, context);
        // Retrieve data from internet || shared preferences || Exits
        sendRequest();
        // Setting only one creation for this fragment in the activity
        setRetainInstance(true);
    }

    /**
     * Called to create the view hierarchy associated with the fragment.
     * Configures the UI layout with the listview.
     *
     * @param inflater           Instantiates a layout XML file into its corresponding View.
     * @param container          A group of view children.
     * @param savedInstanceState Saved aspects of the app.
     * @return                   A view hierarchy.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // View of the XML layout
        View view = inflater.inflate(R.layout.fragment_main,
                container, false);
        // Get ListView object from xml
        listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(adpt);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                v.vibrate(100);
                // Displays Matter details in a dialog
                mController.displayDetails(position, adpt, context);
            }
        });
        return view;
    }

    /**
     * Called when the activity's onCreate() method has returned.
     * Sets the custom title bar with the matters count.
     *
     * @param savedInstanceState Saved aspects of the app.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Called again to update the activity context(config change)
        context = getActivity();
        mController.setTotalMatters(adpt.getCount(), context);
    }

    /**
     * Retrieves data depending on connectivity or displays network/no data
     * alert.
     */
    private void sendRequest() {
        // Restores the data from Shared Preferencs
        String restoredData = mController.restoreMatter(context);
        // Connection is good - send request
        if(mController.checkNetwork(context)) {
            // Exec async load task
            (new AsyncListViewLoader()).execute(URL);
            // Data available retrieve from device
        } else if(restoredData != null) {
            Toast.makeText(context, "Matters Loaded From Memory!", Toast.LENGTH_LONG).show();
            result = mController.populateList(restoredData);
            // Updates listview
            adpt.setItemList(result);
            adpt.notifyDataSetChanged();
        }
        else {
            // Lock the screen and display alert
            mController.lockScreenOrientation(context);
            mController.displayAlert(context);
        }
    }

    /**
     * AsyncListViewLoader is a inner anonymous class handling async network requests.
     */
    private class AsyncListViewLoader extends AsyncTask<String, Void, List<Matters>> {

        private final ProgressDialog DIALOG = new ProgressDialog(context);

        /**
         * After AsyncTask has completed.
         *
         * @param response List of Matters.
         */
        @Override
        protected void onPostExecute(List<Matters> response) {
            super.onPostExecute(response);
            // Populates the listview with Matters
            adpt.setItemList(response);
            adpt.notifyDataSetChanged();

            mController.setTotalMatters(adpt.getCount(), context);
            // Unlocks screen regarding activity threads and config change
            mController.unlockScreenOrientation(context);

            DIALOG.dismiss();
        }

        /**
         * Before AsyncTask is completed.
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // locks screen regarding activity threads and config change
            mController.lockScreenOrientation(context);

            DIALOG.setMessage("Downloading Matters. Hang Tight...");
            DIALOG.show();
        }

        /**
         * Executes an asynchronous task.
         *
         * @param params Url to connect to the server.
         * @return       List of Matters.
         */
        @Override
        protected List<Matters> doInBackground(String... params) {
            String matterData = "";
            // Response from the server(JSON)
            InputStream inputStream = mController.serverResponse(params);

            try {
                // Convert input stream(JSON) to String
                if(inputStream != null)
                    matterData = mController.convertInputStreamToString(inputStream, context);
                else
                    matterData = "No Data!";
                // Convert String to a list of Matters
                result = mController.populateList(matterData);

                return result;
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            return null;
        }
    }
}