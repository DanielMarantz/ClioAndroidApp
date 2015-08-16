package clio.project.matters;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import clio.project.main.ListAdapter;
import clio.project.main.R;

/**
 * Created by BUTTHAMMER on 15/08/15.
 */
public class MattersFragment extends Fragment {

    private ListView listView;
    private ListAdapter adpt;
    private Vibrator v;
    private static final String url = "https://app.goclio.com/api/v2/matters";
    private ArrayList<Matters> result = new ArrayList<Matters>();
    private MattersController mController = new MattersController();

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        adpt  = new ListAdapter(result, getActivity());

        // Exec async load task
        (new AsyncListViewLoader()).execute(url);

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
    //            matterDetails(position);
                Log.d("dataRequest", "Position " + position);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mController.totalMatters(getActivity(), result.size());
    }


    private class AsyncListViewLoader extends AsyncTask<String, Void, List<Matters>> {

        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPostExecute(List<Matters> result) {
            super.onPostExecute(result);
            adpt.setItemList(result);
            mController.totalMatters(getActivity(), result.size());
            unlockScreenOrientation();
            dialog.dismiss();
            adpt.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lockScreenOrientation();
            dialog.setMessage("Downloading Matters. Hang Tight...");
            dialog.show();
        }

        private void lockScreenOrientation() {
            int currentOrientation = getResources().getConfiguration().orientation;

            if (currentOrientation == Configuration.ORIENTATION_PORTRAIT)
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            else
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        private void unlockScreenOrientation() {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }

        @Override
        protected List<Matters> doInBackground(String... params) {
            String matterData = "";
            result = new ArrayList<Matters>();

            try {
                URL u = new URL(params[0]);

                HttpURLConnection conn = (HttpURLConnection) u.openConnection();

                conn.setRequestProperty ("Authorization", "Bearer Xzd7LAtiZZ6HBBjx0DVRqalqN8yjvXgzY5qaD15a");
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.connect();

                InputStream inputStream = conn.getInputStream();

                // convert inputstream to string
                if(inputStream != null)
                    matterData = mController.convertInputStreamToString(inputStream);
                else
                    matterData = "Did not work!";

                JSONObject jsnObject = new JSONObject(matterData);
                JSONArray jsonArray = jsnObject.getJSONArray("matters");

                for (int i=0; i < jsonArray.length(); i++)
                    result.add(mController.convertMatter(getActivity(), jsonArray.getJSONObject(i)));

                return result;
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            return null;
        }
    }
}