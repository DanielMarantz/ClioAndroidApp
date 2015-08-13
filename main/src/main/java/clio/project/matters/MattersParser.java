package clio.project.matters;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by BUTTHAMMER on 12/08/15.
 */
public class MattersParser {

    public String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    public Matters convertMatter(JSONObject obj) throws JSONException {
        JSONObject nestedObj = (JSONObject)obj.get("client");

        String displayName = obj.getString("display_number");
        String clientName = nestedObj.getString("name");
        String description = obj.getString("description");
        String openDate = obj.getString("open_date");
        String status = obj.getString("status");

        //         Log.d("dataRequest", displayName); // TESSSSSSTTTTTTT
        //         Log.d("1dataRequest", clientName);
        //         Log.d("2dataRequest", description);
        //         Log.d("3dataRequest", openDate);
        //         Log.d("4dataRequest", status);

        return new Matters(displayName, clientName, description, openDate, status);
    }
}
