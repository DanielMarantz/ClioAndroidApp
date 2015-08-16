package clio.project.matters;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
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
import clio.project.main.Network;
import clio.project.main.R;

/**
 * Created by Daniel Marantz on 15/08/15.
 */
public class MattersFragment extends Fragment {

    private ListView listView;
    private ListAdapter adpt;
    private Vibrator v;
    private ArrayList<Matters> result = new ArrayList<Matters>();
    private MattersController mController = new MattersController();
    private Network network = new Network();
    private static final String url = "https://app.goclio.com/api/v2/matters";

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        adpt  = new ListAdapter(result, getActivity());

        // Retrieve data from internet || shared preferences || Exits
        sendRequest();
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
                mController.matterDetails(position, getActivity(), adpt);
                Log.d("dataRequest", "Position " + position);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mController.setTotalMatters(getActivity(), result.size());
    }

    public void sendRequest() {
        String restoredData;
        //Connection is good - send request
        if(network.isInternet(getActivity())) {
            // Exec async load task
            (new AsyncListViewLoader()).execute(url);
        } else if(mController.restoreMatter(getActivity()) != null) {
            // Restores the data from Shared Preferencs
            Toast.makeText(getActivity(), "Matters Loaded From Memory!", Toast.LENGTH_LONG).show();
            restoredData = mController.restoreMatter(getActivity());
            result = mController.populateList(restoredData, getActivity());
            adpt.setItemList(result);
            adpt.notifyDataSetChanged();
        }
        else {
            mController.lockScreenOrientation(getActivity());
            displayAlert();
        }
    }

    // Given a String in JSON format convert it to a list of Matters
/*    public ArrayList<Matters> populateList(String matterData) {
        ArrayList<Matters> prefList = new ArrayList<Matters>();

        try {
            JSONObject jsnObject = new JSONObject(matterData);
            JSONArray jsonArray = jsnObject.getJSONArray("matters");

            for (int i=0; i < jsonArray.length(); i++)
                prefList.add(mController.convertMatter(getActivity(), jsonArray.getJSONObject(i)));

            return prefList;
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        return null;
    }
*/
    public void displayAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set title
        alertDialogBuilder.setTitle("No Saved Data Or Network Connection");

        // set dialog message
        alertDialogBuilder
                .setMessage("Need network connection to retrieve initial data!")
                .setCancelable(false)
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        getActivity().finish();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private class AsyncListViewLoader extends AsyncTask<String, Void, List<Matters>> {

        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPostExecute(List<Matters> result) {
            super.onPostExecute(result);
            adpt.setItemList(result);
            mController.setTotalMatters(getActivity(), result.size());
            mController.unlockScreenOrientation(getActivity());
            dialog.dismiss();
            adpt.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mController.lockScreenOrientation(getActivity());
            dialog.setMessage("Downloading Matters. Hang Tight...");
            dialog.show();
        }

        @Override
        protected List<Matters> doInBackground(String... params) {
            String matterData = "";
            result = new ArrayList<Matters>();

            try {
                InputStream inputStream = network.connect(params);

                // convert inputstream to string
                if(inputStream != null) {
                    matterData = mController.convertInputStreamToString(inputStream, getActivity());
                    Log.d("restore5", matterData);
                }
                else
                    matterData = "Did not work!";

                result = mController.populateList(matterData, getActivity());

                return result;
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            return null;
        }
    }
}