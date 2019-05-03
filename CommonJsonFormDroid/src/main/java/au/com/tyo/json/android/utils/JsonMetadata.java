package au.com.tyo.json.android.utils;

import org.json.JSONException;
import org.json.JSONObject;

import au.com.tyo.json.jsonform.JsonFormField;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 20/10/17.
 */

public class JsonMetadata {
    
    public String   key;
    public String   type;
    public String   subtype;
    public int      intType;
    public int      required;

    public JsonMetadata(JSONObject jsonObject) throws JSONException {
        key = jsonObject.getString(JsonFormField.ATTRIBUTE_NAME_KEY);
        type = jsonObject.getString(JsonFormField.ATTRIBUTE_NAME_TYPE);
        required = jsonObject.getInt(JsonFormField.ATTRIBUTE_NAME_REQUIRED);
    }

    @Override
    public String toString() {
        return "JsonMetadata{" +
                "key='" + key + '\'' +
                ", type='" + type + '\'' +
                ", required=" + required +
                '}';
    }
}
