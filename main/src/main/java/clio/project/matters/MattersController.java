package clio.project.matters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import clio.project.main.ListAdapter;
import clio.project.main.R;
import clio.project.main.SharedPreference;

public class MattersController {

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

        saveMatter(context, obj);

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

    // button that sends the user to the Matter Details
    public void matterDetails(int position, Context context, ListAdapter adpt) {
        // custom dialog
        final Dialog dialog = new Dialog(context);

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

    public void lockScreenOrientation(Context context) {
        int currentOrientation = context.getResources().getConfiguration().orientation;

        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT)
            ((Activity)(context)).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
            ((Activity)(context)).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public void unlockScreenOrientation(Context context) {
        ((Activity)(context)).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }
}
