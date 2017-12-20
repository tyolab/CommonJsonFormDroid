package au.com.tyo.json.android.widgets;

import android.view.View;

import au.com.tyo.json.android.R;
import au.com.tyo.json.android.utils.JsonMetadata;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 26/7/17.
 */

class CommonItemFactory {

    protected void setViewTags(View v, JSONObject jsonObject) throws JSONException {
        setViewTags(v, new JsonMetadata(jsonObject));
    }

    protected void setViewTags(View v, JsonMetadata metadata) {
        v.setTag(R.id.key, metadata.key);
        v.setTag(R.id.type, metadata.type);
        v.setTag(R.id.required, metadata.required);
    }
}
