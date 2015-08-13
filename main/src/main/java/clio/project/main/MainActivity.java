package clio.project.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import clio.project.matters.Matters;

public class MainActivity extends Activity {

    ListView listView;
    private ListAdapter adpt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
*/
        adpt  = new ListAdapter(new ArrayList<Matters>(), this);
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adpt);

        // Exec async load task
        (new AsyncListViewLoader()).execute("https://app.goclio.com/api/v2/matters");

        /*
        // Defined Array values to show in ListView
        String[] values = new String[]{"Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"
        };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });
*/
    }

    private class AsyncListViewLoader extends AsyncTask<String, Void, List<Matters>> {
        private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPostExecute(List<Matters> result) {
            super.onPostExecute(result);
            dialog.dismiss();
            adpt.setItemList(result);
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
            List<Matters> result1 = new ArrayList<Matters>();
            String result = "";

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
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";

                JSONObject jsnObject = new JSONObject(result);
                JSONArray jsonArray = jsnObject.getJSONArray("matters");

                for (int i=0; i < jsonArray.length(); i++) {
                    JSONObject test = jsonArray.getJSONObject(i);
       //             Log.d("dataRequest", "2" + test.toString());

                    result1.add(convertMatter(jsonArray.getJSONObject(i)));
                }

                return result1;
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            return null;
        }

        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;
        }

        private Matters convertMatter(JSONObject obj) throws JSONException {
            String displayName = obj.getString("display_number");
            String clientName = "";//= obj.getString("name");
            String description = ""; //= obj.getString("description");
            String openDate = ""; //= obj.getString("open_date");
            String status = ""; //= obj.getString("status");

            Log.d("dataRequest", displayName); // TESSSSSSTTTTTTT
   //         Log.d("1dataRequest", clientName);
   //         Log.d("2dataRequest", description);
   //         Log.d("3dataRequest", openDate);
   //         Log.d("4dataRequest", status);

            return new Matters(displayName, clientName, description, openDate, status);
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
 /*   public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
*/
}
