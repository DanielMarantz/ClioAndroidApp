package clio.project.matters;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Daniel Marantz on 18/08/15.
 *
 * Parses JSON data into Matters Data
 */
public class MattersParser {

    /**
     * Converts a JSON object into a Matters object.
     *
     * @param obj            JSON object to be converted.
     * @return               Instantiated Matters object.
     * @throws org.json.JSONException
     */
    protected Matters parse(JSONObject obj) throws JSONException {
        Matters matter = new Matters();

        if (obj.has("display_number")) {
            matter.setDisplayName(obj.getString("display_number"));
        }
        if (obj.has("client")) {
            JSONObject nestedObj = (JSONObject)obj.get("client");
            matter.setClientName(nestedObj.getString("name"));
        }
        if (obj.has("description")) {
            matter.setDescription(obj.getString("description"));
        }
        if (obj.has("open_date")) {
            matter.setOpenDate(obj.getString("open_date"));
        }
        if (obj.has("status")) {
            matter.setStatus(obj.getString("status"));
        }

        return matter;
    }
}
