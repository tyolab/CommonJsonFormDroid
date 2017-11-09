package au.com.tyo.json.android.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 20/10/17.
 */

public class JsonMetadata {
    
    public String   key;
    public String   type;
    public String   subtype;
    public int      intType;
    public boolean  required;

    public JsonMetadata(JSONObject jsonObject) throws JSONException {
        key = jsonObject.getString("key");
        type = jsonObject.getString("type");
        required = jsonObject.getBoolean("required");
    }
}
