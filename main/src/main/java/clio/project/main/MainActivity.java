package clio.project.main;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import clio.project.matters.Matters;
import clio.project.matters.MattersParser;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 /*       setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            ImagesFragment fragmentDemo = (ImagesFragment)
                    getFragmentManager().findFragmentById(R.id.listFragment);
        }
*/
        // custom title bar
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                R.layout.customtitlebar);

        /*
        if(savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.listFragment, new ImagesFragment())
                    .commit();
        }
*/

    }





    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ImagesFragment extends Fragment {

        int test = 0;
        ListView listView;
        private ListAdapter adpt;
        Vibrator v;
        TextView totalMatters;
        private static final String url = "https://app.goclio.com/api/v2/matters";
//        Activity context = getActivity();
        ArrayList<Matters> result;
        MattersParser parser = new MattersParser();

        public ImagesFragment() {
        }

        // this method is only called once for this fragment
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if(test == 0) {
                // Exec async load task
                (new AsyncListViewLoader()).execute(url);
            }
            test = 1;

            setRetainInstance(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_main,
                    container, false);

            v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

            // Get ListView object from xml
            listView = (ListView) view.findViewById(R.id.list);

            //////////////////////////////////////////////
            if(result.size() == 0) {
                adpt  = new ListAdapter(new ArrayList<Matters>(), getActivity());
            }


            listView.setAdapter(adpt);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {

                    v.vibrate(100);
                    matterDetails(position);
                    Log.d("dataRequest", "Position " + position);
                }
            });

            return view;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            parser.totalMatters(getActivity(), result.size());
        }

        // button that sends the user to the Matter Details
        public void matterDetails(int position) {

            // custom dialog
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.matter_details);
            dialog.setTitle("DETAILS");

            // set the custom dialog components - text, button
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

            //Close dialog button
            Button closeButton = (Button) dialog.findViewById(R.id.closeDialog);

            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

        private class AsyncListViewLoader extends AsyncTask<String, Void, List<Matters>> {
            private final ProgressDialog dialog = new ProgressDialog(getActivity());

            @Override
            protected void onPostExecute(List<Matters> result) {
                super.onPostExecute(result);
                adpt.setItemList(result);
                parser.totalMatters(getActivity(), result.size());
                dialog.dismiss();
                adpt.notifyDataSetChanged();
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.setMessage("Downloading Matters...");
                dialog.show();
            }

            @Override
            protected List<Matters> doInBackground(String... params) {
                result = new ArrayList<Matters>();

                String matterData = "";

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
                        matterData = parser.convertInputStreamToString(inputStream);
                    else
                        matterData = "Did not work!";

                    JSONObject jsnObject = new JSONObject(matterData);
                    JSONArray jsonArray = jsnObject.getJSONArray("matters");

                    for (int i=0; i < jsonArray.length(); i++) {
                        //               JSONObject test = jsonArray.getJSONObject(i);
                        //                Log.d("dataRequest", "3" + test.toString());

                        result.add(parser.convertMatter(getActivity(), jsonArray.getJSONObject(i)));
                    }

                    return result;
                }
                catch(Throwable t) {
                    t.printStackTrace();
                }
                return null;
            }
        }
    }
}
