package clio.project.matters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import clio.project.main.R;
import clio.project.main.SharedPreference;

/**
 * Created by BUTTHAMMER on 12/08/15.
 */
public class MattersParser {

    public static final String PREFS_NAME = "<MATTERS_PREFS";
    public static final String PREFS_KEY = "AOP_PREFS_String";
    public SharedPreference mattersDatabase = new SharedPreference();

    public String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    public Matters convertMatter(Context context, JSONObject obj) throws JSONException {
        JSONObject nestedObj = (JSONObject)obj.get("client");

        String displayName = obj.getString("display_number");
        String clientName = nestedObj.getString("name");
        String description = obj.getString("description");
        String openDate = obj.getString("open_date");
        String status = obj.getString("status");



                 Log.d("dataRequest", obj.getString("practice_area")); // TESSSSSSTTTTTTT
        //         Log.d("1dataRequest", clientName);
        //         Log.d("2dataRequest", description);
        //         Log.d("3dataRequest", openDate);
        //         Log.d("4dataRequest", status);

 //       saveMatter(context, obj);

        return new Matters(displayName, clientName, description, openDate, status);
    }

    public void saveMatter(Context context, JSONObject jsonObject) {
        mattersDatabase.save(context,jsonObject.toString());
    }

    // text of total matters in the custom title bar
    public void totalMatters(Context context, int total) {
        TextView totalMatters = (TextView)((Activity)(context)).findViewById(R.id.totalNumber);
        totalMatters.setText("" + total);
    }

}
